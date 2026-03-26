package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

import test.RequestParser.RequestInfo;

public class MyHTTPServer extends Thread implements HTTPServer {

    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private volatile boolean running;
    private Map<String, Map<String, Servlet>> servletMap;

    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(nThreads);
        this.running = false;
        this.servletMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        servletMap.putIfAbsent(httpCommand, new ConcurrentHashMap<>());
        servletMap.get(httpCommand).put(uri, s);
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        Map<String, Servlet> innerMap = servletMap.get(httpCommand);
        if (innerMap != null) {
            innerMap.remove(uri);
        }
    }

    @Override
    public void run() {
        if (running) return;
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
        try (
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream()
        ) {
            RequestInfo ri = RequestParser.parseRequest(new BufferedReader(new InputStreamReader(in)));
            if (ri == null) {
                out.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                return;
            }

            Map<String, Servlet> innerMap = servletMap.get(ri.getHttpCommand());
            if (innerMap == null) {
                out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                return;
            }

            // Find best matching servlet (longest match)
            Servlet servlet = null;
            String bestMatch = "";
            for (String uriPattern : innerMap.keySet()) {
                if (ri.getUri().startsWith(uriPattern) && uriPattern.length() > bestMatch.length()) {
                    bestMatch = uriPattern;
                    servlet = innerMap.get(uriPattern);
                }
            }

            if (servlet == null) {
                out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                return;
            }

            servlet.handle(ri, out);

        } catch (Exception e) {
            try {
                OutputStream out = client.getOutputStream();
                out.write("HTTP/1.1 500 Internal Server Error\r\n\r\n".getBytes());
            } catch (IOException ignored) {}
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void close() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map<String, Servlet> innerMap : servletMap.values()) {
            for (Servlet servlet : innerMap.values()) {
                try {
                    servlet.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        executor.shutdown();
    }
}
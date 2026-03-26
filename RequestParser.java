package test;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null || line.isEmpty()) return null;

        String[] parts = line.split(" ");
        if (parts.length < 2) return null;

        String method = parts[0];
        String fullUri = parts[1];
        String uri = fullUri.contains("?") ? fullUri.substring(0, fullUri.indexOf("?")) : fullUri;

        String[] uriSegments = Arrays.stream(uri.split("/"))
                                     .filter(s -> !s.isEmpty())
                                     .toArray(String[]::new);

        Map<String, String> parameters = new LinkedHashMap<>();

        if (fullUri.contains("?")) {
            String query = fullUri.substring(fullUri.indexOf("?") + 1);
            for (String param : query.split("&")) {
                String[] kv = param.split("=", 2);
                if (kv.length == 2)
                    parameters.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
                else if (kv.length == 1)
                    parameters.put(URLDecoder.decode(kv[0], "UTF-8"), "");
            }
        }

        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        byte[] content = new byte[0];
        if (contentLength > 0) {
            // קרא פרמטר ליין
            String paramLine = reader.readLine();
            if (paramLine != null && paramLine.contains("=")) {
                String[] kv = paramLine.split("=", 2);
                if (kv.length == 2) {
                    parameters.put(kv[0].trim(), kv[1].trim());
                }
            }
            
            // קרא שורה ריקה
            reader.readLine();
            
            // קרא תוכן
            String contentLine = reader.readLine();
            if (contentLine != null) {
                content = (contentLine + "\n").getBytes(StandardCharsets.UTF_8);
            }
        }

        return new RequestInfo(method, fullUri, uriSegments, parameters, content);
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameter;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments,
                           Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameter = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameter;
        }

        public byte[] getContent() {
            return content;
        }
    }
}


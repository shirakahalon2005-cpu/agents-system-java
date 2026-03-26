package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class GenericConfig implements Config {
    private final List<Agent> agents = new ArrayList<>();
    private String fileName;

    public void setConfFile(String fileName) {
        this.fileName = fileName;
    }

    public void create() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            for (int i = 0; i < lines.size(); i += 3) {
                String className = lines.get(i);
                String[] subs = lines.get(i + 1).split(",");
                String[] pubs = lines.get(i + 2).split(",");

                Class<?> cls = Class.forName(className);
                Constructor<?> ctor = cls.getConstructor(String[].class, String[].class);
                Agent agent = (Agent) ctor.newInstance(subs, pubs);
                agents.add(new ParallelAgent(agent));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create config", e);
        }
    }

    public String getName() {
        return "Generic Config";
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
        for (Agent agent : agents) {
            agent.close();
        }
    }
}


package test;

import java.util.ArrayList;
import java.util.List;




class Topic {
    public final String name;
    private final List<Agent> subs = new ArrayList<>();
    private final List<Agent> pubs = new ArrayList<>();

    Topic(String name) {
        this.name = name;
    }

    void subscribe(Agent agent) {
        subs.add(agent);
    }

    void unsubscribe(Agent agent) {
        subs.remove(agent);
    }

    void publish(Message msg) {
        for (Agent agent : subs) {
            agent.callback(name, msg);
        }
    }

    void addPublisher(Agent agent) {
        pubs.add(agent);
    }

    void removePublisher(Agent agent) {
        pubs.remove(agent);
    }

    public List<Agent> getSubscribers() {
        return subs;
    }

    public List<Agent> getPublishers() {
        return pubs;
    }
}

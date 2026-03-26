package test;

import java.util.UUID;

import test.TopicManagerSingleton.TopicManager;

class IncAgent implements Agent {
    private final String name;
    private final String[] subs;
    private final String[] pubs;

    public IncAgent(String[] subs, String[] pubs) {
        this.name = "Inc" + UUID.randomUUID();
        this.subs = subs;
        this.pubs = pubs;
        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).subscribe(this);
        tm.getTopic(pubs[0]).addPublisher(this);
    }

    public String getName() { return name; }
    public void reset() {}

    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            double result = msg.asDouble + 1;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(result));
        }
    }

    public void close() {}
}


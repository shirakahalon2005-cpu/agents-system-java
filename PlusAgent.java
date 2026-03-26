package test;

import java.util.UUID;

import test.TopicManagerSingleton.TopicManager;

class PlusAgent implements Agent {
    private final String name;
    private final String[] subs;
    private final String[] pubs;
    private Message x = new Message(0.0), y = new Message(0.0);

    public PlusAgent(String[] subs, String[] pubs) {
        this.name = "Plus" + UUID.randomUUID();
        this.subs = subs;
        this.pubs = pubs;
        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).subscribe(this);
        tm.getTopic(subs[1]).subscribe(this);
        tm.getTopic(pubs[0]).addPublisher(this);
    }

    public String getName() { return name; }
    public void reset() { x = y = new Message(0.0); }

    public void callback(String topic, Message msg) {
        if (topic.equals(subs[0])) x = msg;
        else if (topic.equals(subs[1])) y = msg;
        if (!Double.isNaN(x.asDouble) && !Double.isNaN(y.asDouble)) {
            double result = x.asDouble + y.asDouble;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(result));
        }
    }

    public void close() {}
}


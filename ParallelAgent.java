package test;



public class ParallelAgent implements Agent {
    private final Agent agent;
    private final Thread thread;

    public ParallelAgent(Agent agent) {
        this.agent = agent;
        this.thread = new Thread(() -> {}); // ניתן להוסיף פעולה אם צריך
        this.thread.start();
    }

    @Override
    public String getName() {
        return agent.getName();
    }

    @Override
    public void reset() {
        agent.reset();
    }

    @Override
    public void callback(String topic, Message msg) {
        agent.callback(topic, msg);
    }

    @Override
    public void close() {
        agent.close();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // משחזר את מצב ההפרעה
        }
    }

    
}

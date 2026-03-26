package test;

import java.util.function.BinaryOperator;

import test.TopicManagerSingleton.TopicManager;

class BinOpAgent implements Agent {
    private final String name;
    private final String input1, input2, output;
    private final BinaryOperator<Double> op;
    private Message msg1, msg2;

    public BinOpAgent(String name, String input1, String input2, String output, BinaryOperator<Double> op) {
        this.name = name;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.op = op;

        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(input1).subscribe(this);
        tm.getTopic(input2).subscribe(this);
        tm.getTopic(output).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
        msg1 = msg2 = new Message(0.0);
    }

    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(input1)) {
            msg1 = msg;
        } else if (topic.equals(input2)) {
            msg2 = msg;
        }
        if (msg1 != null && msg2 != null) {
            double result = op.apply(msg1.asDouble, msg2.asDouble);
            TopicManagerSingleton.get().getTopic(output).publish(new Message(result));
        }
    }

    @Override
    public void close() {}
}
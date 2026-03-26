// גרסה סופית: שומרת את כל הצמתים לקשרי מחזוריות אך מחזירה רק את ה־Topics הנדרשים
package test;



import java.util.*;



class Graph implements Iterable<Node> {
    final List<Node> nodes = new ArrayList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public boolean hasCycles() {
        for (Node node : nodes) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return nodes.size();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public void createFromTopics() {
        Map<String, Node> nodeMap = new HashMap<>();
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        for (Topic topic : tm.getTopics()) {
            String topicName = "T" + topic.name;
            Node topicNode = nodeMap.computeIfAbsent(topicName, Node::new);

            for (Agent sub : topic.getSubscribers()) {
                String agentName = "A" + sub.getName();
                Node agentNode = nodeMap.computeIfAbsent(agentName, Node::new);
                topicNode.addEdge(agentNode); // Topic -> Agent
            }

            for (Agent pub : topic.getPublishers()) {
                String agentName = "A" + pub.getName();
                Node agentNode = nodeMap.computeIfAbsent(agentName, Node::new);
                agentNode.addEdge(topicNode); // Agent -> Topic
            }
        }

        this.nodes.clear();
        this.nodes.addAll(nodeMap.values());
    }
}
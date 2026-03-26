package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import test.Message;


class Node {
    private final String name;
    private final List<Node> edges = new ArrayList<>();
    private String message;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Node> getEdges() {
        return edges;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addEdge(Node node) {
        edges.add(node);
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        return hasCyclesHelper(this, visited);
    }

    private boolean hasCyclesHelper(Node node, Set<Node> visited) {
        if (visited.contains(node)) {
            return true;
        }
        visited.add(node);
        for (Node neighbor : node.getEdges()) {
            if (neighbor.hasCyclesHelper(neighbor, new HashSet<>(visited))) {
                return true;
            }
        }
        return false;
    }
}

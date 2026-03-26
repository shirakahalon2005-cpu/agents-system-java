package test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import test.Topic;

// TopicManagerSingleton class with inner TopicManager
public class TopicManagerSingleton {

    // מחזיר את המופע היחיד של TopicManager
    public static TopicManager get() {
        return TopicManagerHolder.instance;
    }

    // מחלקת TopicManager הפנימית
    public static class TopicManager {
        private final Map<String, Topic> topics = new HashMap<>();

        public TopicManager() {
            // אתחול map
        }

        public Topic getTopic(String name) {
            Topic topic = topics.get(name);
            if (topic == null) {
                topic = new Topic(name);
                topics.put(name, topic);
            }
            return topic;
        }

        public Collection<Topic> getTopics() {
            return topics.values();
        }

        public void clear() {
            topics.clear();
        }
    }

    // מחזיק את המופע הבודד
    private static class TopicManagerHolder {
        private static final TopicManager instance = new TopicManager();
    }
}

package com.filesearch.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lion
 * @date 2025/2/2 22:34
 */
public class EventBus {
    private static EventBus instance;
    private final Map<Class<?>, Subscriber<?>> subscribers = new ConcurrentHashMap<>();

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public <T> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {
        subscribers.put(eventType, subscriber);
    }

    public <T> void publish(T event) {
        Subscriber<?> subscriber = subscribers.get(event.getClass());
        if (subscriber != null) {
            ((Subscriber<T>) subscriber).onEvent(event);
        }
    }

    public interface Subscriber<T> {
        void onEvent(T event);
    }
}

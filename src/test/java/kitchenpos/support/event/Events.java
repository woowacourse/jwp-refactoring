package kitchenpos.support.event;

import java.util.LinkedList;
import java.util.List;
import org.springframework.context.event.EventListener;

public class Events {

    private final List<Object> events = new LinkedList<>();

    @EventListener
    public void addEvent(Object event) {
        events.add(event);
    }

    public <T> Integer count(final Class<T> clazz) {
        return Math.toIntExact(events.stream()
                .filter(clazz::isInstance)
                .count());
    }

    public void clear() {
        events.clear();
    }
}

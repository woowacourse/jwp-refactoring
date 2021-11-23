package kitchenpos.order.domain;

public class OrderPlacedEvent {

    private final Order order;

    public OrderPlacedEvent(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}

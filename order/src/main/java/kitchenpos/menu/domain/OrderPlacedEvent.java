package kitchenpos.menu.domain;

public class OrderPlacedEvent implements OrderTableOrderedEvent {

    private final Order order;

    public OrderPlacedEvent(final Order order) {
        this.order = order;
    }

    @Override
    public Long getOrderTableId() {
        return order.getOrderTableId();
    }
}

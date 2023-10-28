package kitchenpos.order.domain;

public class MenuDetailsSnapshotEvent {

    private final Order order;

    public MenuDetailsSnapshotEvent(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}

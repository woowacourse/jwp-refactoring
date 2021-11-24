package kitchenpos.menu.domain;

public class OrderTableEmptyChangedEvent {

    private final OrderTable orderTable;

    public OrderTableEmptyChangedEvent(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}

package kitchenpos.ordertable.domain;

public class UngroupEvent {
    private final OrderTables orderTables;

    public UngroupEvent(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getGroupedTables() {
        return orderTables;
    }
}

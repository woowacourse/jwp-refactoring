package kitchenpos.ordertable.domain;

public class UnGroupEvent {
    private final OrderTables orderTables;

    public UnGroupEvent(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getGroupedTables() {
        return orderTables;
    }
}

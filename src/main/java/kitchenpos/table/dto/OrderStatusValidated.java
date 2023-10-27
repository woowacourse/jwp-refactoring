package kitchenpos.table.dto;

public class OrderStatusValidated {

    private final Long orderTableId;

    public OrderStatusValidated(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}

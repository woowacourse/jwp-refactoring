package kitchenpos.common.event.message;

public class ValidatorOrderTable {

    private final Long orderTableId;

    public ValidatorOrderTable(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}

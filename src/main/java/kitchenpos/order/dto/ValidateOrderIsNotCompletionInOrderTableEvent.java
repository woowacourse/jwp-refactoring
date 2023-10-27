package kitchenpos.order.dto;

public class ValidateOrderIsNotCompletionInOrderTableEvent {

    private final Long tableGroupId;

    public ValidateOrderIsNotCompletionInOrderTableEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}

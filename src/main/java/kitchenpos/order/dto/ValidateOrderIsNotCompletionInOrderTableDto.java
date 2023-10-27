package kitchenpos.order.dto;

public class ValidateOrderIsNotCompletionInOrderTableDto {

    private Long tableGroupId;

    public ValidateOrderIsNotCompletionInOrderTableDto(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}

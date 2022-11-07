package kitchenpos.application.table.dto.request;

public class EmptyTableDto {

    private final Long orderTableId;
    private final Boolean empty;

    public EmptyTableDto(Long orderTableId, Boolean empty) {
        this.orderTableId = orderTableId;
        this.empty = empty;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

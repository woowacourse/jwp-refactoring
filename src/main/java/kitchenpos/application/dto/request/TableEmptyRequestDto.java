package kitchenpos.application.dto.request;

public class TableEmptyRequestDto {

    private Long orderTableId;
    private Boolean empty;

    private TableEmptyRequestDto() {
    }

    public TableEmptyRequestDto(Long orderTableId, Boolean empty) {
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

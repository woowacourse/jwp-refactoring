package kitchenpos.order.dto;

public class UpdateUngroupOrderTableDto {

    private Long tableGroupId;

    public UpdateUngroupOrderTableDto(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}

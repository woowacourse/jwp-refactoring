package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class TableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    private TableResponse(final Long id, final Long tableGroupId, final Integer numberOfGuests, final Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(final OrderTable table) {
        return new TableResponse(
                table.getId(),
                table.getTableGroupId(),
                table.getNumberOfGuests(),
                table.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}

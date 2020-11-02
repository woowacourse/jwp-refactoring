package kitchenpos.table.dto;

import kitchenpos.table.domain.Table;

public class TableResponse {

    private final Long id;
    private final TableGroupResponse tableGroupResponse;
    private final Integer tableNumberOfGuests;
    private final boolean empty;

    private TableResponse(Long id, TableGroupResponse tableGroupResponse, Integer tableNumberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupResponse = tableGroupResponse;
        this.tableNumberOfGuests = tableNumberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(Table table) {
        TableGroupResponse tableGroupResponse = TableGroupResponse.from(table.getTableGroup());
        return new TableResponse(table.getId(), tableGroupResponse,
            table.getTableNumberOfGuests().getNumberOfGuests(), table.getTableEmpty().getEmpty());
    }

    public Long getId() {
        return id;
    }

    public TableGroupResponse getTableGroupReponse() {
        return tableGroupResponse;
    }

    public Integer getTableNumberOfGuests() {
        return tableNumberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

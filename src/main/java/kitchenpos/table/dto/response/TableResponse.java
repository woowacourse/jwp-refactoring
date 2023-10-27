package kitchenpos.table.dto.response;

import kitchenpos.table.domain.OrderTable;

public class TableResponse {

    private final Long id;

    private final Long tableGroupId;

    private final int numberOfGuests;

    private final boolean empty;

    private TableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(final OrderTable table) {
        if (table.getTableGroup() == null) {
            return new TableResponse(
                    table.getId(),
                    null,
                    table.getNumberOfGuests(),
                    table.isEmpty()
            );
        }

        return new TableResponse(
                table.getId(),
                table.getTableGroup().getId(),
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

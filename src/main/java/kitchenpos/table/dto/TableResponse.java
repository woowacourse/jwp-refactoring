package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;

public class TableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public TableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse of(final OrderTable orderTable) {
        if (orderTable.getTableGroup() != null) {
            return new TableResponse(orderTable.getId(), orderTable.getTableGroup().getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }
        return new TableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<TableResponse> ofList(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::of)
                .collect(Collectors.toList());
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

    public boolean getEmpty() {
        return empty;
    }
}

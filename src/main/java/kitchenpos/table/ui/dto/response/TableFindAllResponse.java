package kitchenpos.table.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class TableFindAllResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public TableFindAllResponse() {
    }

    private TableFindAllResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
                                 final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<TableFindAllResponse> from(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableFindAllResponse::from)
                .collect(Collectors.toList());
    }

    private static TableFindAllResponse from(final OrderTable orderTable) {
        return new TableFindAllResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
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

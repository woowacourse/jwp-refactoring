package kitchenpos.table.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class TableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private TableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse of(OrderTable table) {
        Long tableGroupId = null;
        if (Objects.nonNull(table.getTableGroup())) {
            tableGroupId = table.getTableGroup().getId();
        }
        return new TableResponse(
                table.getId(),
                tableGroupId,
                table.getNumberOfGuests(),
                table.isEmpty()
        );
    }

    public static List<TableResponse> listOf(List<OrderTable> tables) {
        return tables.stream()
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

    public boolean isEmpty() {
        return empty;
    }
}

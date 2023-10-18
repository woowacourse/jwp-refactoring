package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class TableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean isEmpty;

    private TableResponse(
            Long id,
            Long tableGroupId,
            Integer numberOfGuests,
            Boolean isEmpty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                orderTable.getTableGroup().getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<TableResponse> from(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
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

    public Boolean getEmpty() {
        return isEmpty;
    }
}

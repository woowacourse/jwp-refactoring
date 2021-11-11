package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        Long tableGroupId = null;
        if (Objects.nonNull(orderTable.getTableGroup())) {
            tableGroupId = orderTable.getTableGroup().getId();
        }
        return new OrderTableResponse(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> toList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
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

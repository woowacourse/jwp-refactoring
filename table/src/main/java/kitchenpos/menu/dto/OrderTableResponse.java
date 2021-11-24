package kitchenpos.menu.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    public OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
                              final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTableResponse> listFrom(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        Long tableGroupId = null;
        if (Objects.nonNull(orderTable.getTableGroup())) {
            tableGroupId = orderTable.getTableGroup().getId();
        }

        return new OrderTableResponse(
            orderTable.getId(),
            tableGroupId,
            orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty()
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

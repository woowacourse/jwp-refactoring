package kitchenpos.ui.dto.response;

import kitchenpos.domain.OrderTable;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderTableResponse {

    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    private OrderTableResponse(
            Long id,
            Long tableGroup,
            int numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse create(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> of(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::create)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

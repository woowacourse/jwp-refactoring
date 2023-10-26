package kitchenpos.table.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private final Long id;

    private final int numberOfGuests;

    private final boolean empty;

    public OrderTableResponse(final Long id,
                              final int numberOfGuests,
                              final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                                      orderTable.getNumberOfGuests(),
                                      orderTable.isEmpty());
    }

    public static List<OrderTableResponse> convertToList(final List<OrderTable> orderTables) {
        return orderTables.stream()
                          .map(OrderTableResponse::from)
                          .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

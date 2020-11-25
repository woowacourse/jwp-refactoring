package kitchenpos.application.response;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.domain.model.ordertable.OrderTable;

public class OrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public static List<OrderTableResponse> listOf(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
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

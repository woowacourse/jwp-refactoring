package kitchenpos.dto.response.table;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.response.OrderResponse;

public class TableInGroupResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    private List<OrderResponse> orders;

    public TableInGroupResponse(Long id, int numberOfGuests, boolean empty, List<OrderResponse> orders) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public static TableInGroupResponse from(OrderTable orderTable) {
        return new TableInGroupResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty(),
                orderResponse(orderTable)
        );
    }

    private static List<OrderResponse> orderResponse(OrderTable orderTable) {
        return orderTable.getOrders().stream()
                         .map(OrderResponse::from)
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

    public List<OrderResponse> getOrders() {
        return orders;
    }
}

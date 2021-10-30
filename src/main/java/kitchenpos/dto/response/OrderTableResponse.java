package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    private List<OrderResponse> orders;

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty, List<OrderResponse> orders) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
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

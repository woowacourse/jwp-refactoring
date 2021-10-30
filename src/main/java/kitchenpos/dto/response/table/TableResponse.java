package kitchenpos.dto.response.table;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.response.OrderResponse;

public class TableResponse {
    private Long id;
    private TableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;
    private List<OrderResponse> orders;

    public TableResponse(Long id, TableGroupResponse tableGroupResponse, int numberOfGuests, boolean empty, List<OrderResponse> orders) {
        this.id = id;
        this.tableGroup = tableGroupResponse;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                TableGroupResponse.from(orderTable.getTableGroup()),
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

    public TableGroupResponse getTableGroup() {
        return tableGroup;
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

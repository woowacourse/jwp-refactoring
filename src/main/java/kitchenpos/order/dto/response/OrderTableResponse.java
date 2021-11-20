package kitchenpos.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> toList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    protected OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}

package kitchenpos.application.dto.response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        final TableGroup tableGroup = orderTable.getTableGroup();
        final Long tableGroupId = Objects.isNull(tableGroup) ? null : tableGroup.getId();
        return new OrderTableResponse(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests().getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> from(OrderTables orderTables) {
        return from(orderTables.getValue());
    }

    public static List<OrderTableResponse> from(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::from)
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

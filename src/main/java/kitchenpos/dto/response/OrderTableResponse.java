package kitchenpos.dto.response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

public class OrderTableResponse {

    private final long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(final long id,
                               final Long tableGroupId,
                               final int numberOfGuests,
                               final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        final TableGroup tableGroup = orderTable.getTableGroup();
        if (Objects.isNull(tableGroup)) {
            return new OrderTableResponse(
                    orderTable.getId(),
                    null,
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
            );
        }
        return new OrderTableResponse(
                orderTable.getId(),
                tableGroup.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> of(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
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

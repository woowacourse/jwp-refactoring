package kitchenpos.core.ordertable.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.core.ordertable.domain.NumberOfGuests;
import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.ordertable.domain.OrderTables;

public class OrderTableResponse {
    @JsonProperty
    private final Long id;
    @JsonProperty
    private final Long tableGroupId;
    @JsonUnwrapped
    private final NumberOfGuests numberOfGuests;
    @JsonProperty
    private final boolean empty;

    @JsonCreator
    public OrderTableResponse(final Long id, final Long tableGroupId, final NumberOfGuests numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public static List<OrderTableResponse> from(final OrderTables orderTables) {
        final ArrayList<OrderTableResponse> orderTableResponses = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            orderTableResponses.add(from(orderTable));
        }
        return orderTableResponses;
    }
}

package kitchenpos.table.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("tableGroupId")
    private final Long tableGroupId;
    @JsonProperty("numberOfGuests")
    private final int numberOfGuests;
    @JsonProperty("empty")
    private final boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.id(),
                orderTable.tableGroup().id(),
                orderTable.numberOfGuests(),
                orderTable.empty()
        );
    }
}

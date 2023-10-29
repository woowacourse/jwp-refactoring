package kitchenpos.table.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.OrderTable;

public class CreateOrderTableResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("tableGroupId")
    private final Long tableGroupId;
    @JsonProperty("numberOfGuests")
    private final int numberOfGuests;
    @JsonProperty("empty")
    private final boolean empty;

    private CreateOrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    private CreateOrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static CreateOrderTableResponse from(OrderTable orderTable) {
        return new CreateOrderTableResponse(
                orderTable.id(),
                orderTable.numberOfGuests(),
                orderTable.empty()
        );
    }

    public Long id() {
        return id;
    }

    public Long tableGroupId() {
        return tableGroupId;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean empty() {
        return empty;
    }
}

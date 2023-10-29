package kitchenpos.core.ordertable.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kitchenpos.core.ordertable.domain.NumberOfGuests;
import kitchenpos.core.ordertable.domain.OrderTable;

public class TableResponse {
    @JsonProperty
    private Long id;
    @JsonProperty
    private Long tableGroupId;
    @JsonUnwrapped
    private NumberOfGuests numberOfGuests;
    @JsonProperty
    private boolean empty;

    public TableResponse(final Long id, final Long tableGroupId, final NumberOfGuests numberOfGuests,
                         final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(final OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }
}

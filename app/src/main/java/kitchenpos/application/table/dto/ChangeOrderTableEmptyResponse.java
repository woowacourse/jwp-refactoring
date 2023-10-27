package kitchenpos.application.table.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class ChangeOrderTableEmptyResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("tableGroupId")
    private final Long tableGroupId;
    @JsonProperty("numberOfGuests")
    private final int numberOfGuests;
    @JsonProperty("empty")
    private final boolean empty;

    private ChangeOrderTableEmptyResponse(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    private ChangeOrderTableEmptyResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static ChangeOrderTableEmptyResponse from(OrderTable orderTable) {
        Long id = orderTable.id();
        TableGroup tableGroup = orderTable.tableGroup();
        int numberedOfGuests = orderTable.numberOfGuests();
        boolean empty = orderTable.empty();
        if (Objects.isNull(tableGroup)) {
            return new ChangeOrderTableEmptyResponse(id, numberedOfGuests, empty);
        }
        return new ChangeOrderTableEmptyResponse(id, tableGroup.id(), numberedOfGuests, empty);
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

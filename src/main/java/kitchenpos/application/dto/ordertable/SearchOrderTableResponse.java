package kitchenpos.application.dto.ordertable;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class SearchOrderTableResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("tableGroupId")
    private final Long tableGroupId;
    @JsonProperty("numberOfGuests")
    private final int numberOfGuests;
    @JsonProperty("empty")
    private final boolean empty;

    private SearchOrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    private SearchOrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static SearchOrderTableResponse from(OrderTable orderTable) {
        Long id = orderTable.id();
        TableGroup tableGroup = orderTable.tableGroup();
        int numberedOfGuests = orderTable.numberOfGuests();
        boolean empty = orderTable.empty();
        if (Objects.isNull(tableGroup)) {
            return new SearchOrderTableResponse(id, numberedOfGuests, empty);
        }
        return new SearchOrderTableResponse(id, tableGroup.id(), numberedOfGuests, empty);
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

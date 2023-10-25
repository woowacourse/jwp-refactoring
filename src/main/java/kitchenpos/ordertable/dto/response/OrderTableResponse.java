package kitchenpos.ordertable.dto.response;

import io.micrometer.core.lang.Nullable;
import java.util.Objects;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableResponse {
    private final long id;
    @Nullable
    private final TableGroupResponse tableGroupResponse;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(
            final Long id,
            final TableGroupResponse tableGroupResponse,
            final int numberOfGuests,
            final boolean empty
    ) {
        this.id = id;
        this.tableGroupResponse = tableGroupResponse;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.id(),
                parseTableGroup(orderTable.tableGroup()),
                orderTable.numberOfGuests(),
                orderTable.isEmpty()
        );
    }

    @Nullable
    private static TableGroupResponse parseTableGroup(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }

        return TableGroupResponse.from(tableGroup);
    }

    public long getId() {
        return id;
    }

    @Nullable
    public TableGroupResponse getTableGroupResponse() {
        return tableGroupResponse;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}

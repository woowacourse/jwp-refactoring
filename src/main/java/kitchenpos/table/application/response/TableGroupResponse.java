package kitchenpos.table.application.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<TableResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTables.stream()
                        .map(TableResponse::from)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getOrderTables() {
        return orderTables;
    }

    public static class TableResponse {
        private final Long id;
        private final Long tableGroupId;
        private final Integer numberOfGuests;
        private final Boolean empty;

        public TableResponse(final Long id, final Long tableGroupId, final Integer numberOfGuests,
                             final Boolean empty) {
            this.id = id;
            this.tableGroupId = tableGroupId;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public static TableResponse from(final OrderTable orderTable) {
            return new TableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }
    }
}

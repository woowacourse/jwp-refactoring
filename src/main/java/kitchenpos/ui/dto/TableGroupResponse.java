package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableGroupInnerOrderTable> orderTables;

    private TableGroupResponse() {
    }

    public TableGroupResponse(final Long id,
                              final LocalDateTime createdDate,
                              final List<TableGroupInnerOrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                mapToOrderTableResponse(tableGroup.getOrderTables()));
    }

    private static List<TableGroupInnerOrderTable> mapToOrderTableResponse(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableGroupInnerOrderTable::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableGroupInnerOrderTable> getOrderTables() {
        return orderTables;
    }

    public static class TableGroupInnerOrderTable {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private TableGroupInnerOrderTable() {
        }

        private TableGroupInnerOrderTable(final Long id,
                                         final Long tableGroupId,
                                         final int numberOfGuests,
                                         final boolean empty) {
            this.id = id;
            this.tableGroupId = tableGroupId;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public static TableGroupInnerOrderTable from(final OrderTable orderTable) {
            return new TableGroupInnerOrderTable(
                    orderTable.getId(),
                    orderTable.getTableGroupId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
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
}

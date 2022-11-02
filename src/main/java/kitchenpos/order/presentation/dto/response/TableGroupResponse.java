package kitchenpos.order.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    public TableGroupResponse(final LocalDateTime createdDate, final List<OrderTableResponse> orderTablesResponses) {
        this(null, createdDate, orderTablesResponses);
    }

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTablesResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTablesResponses;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {

        final List<OrderTableResponse> orderTableResponses = extractOrderTableResponses(tableGroup);

        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableResponses
        );
    }

    private static List<OrderTableResponse> extractOrderTableResponses(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getTableGroup().getId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty())
                )
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}

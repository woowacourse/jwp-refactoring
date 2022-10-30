package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup, OrderTables orderTables) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                toOrderTablesDto(orderTables.getValues()));
    }

    private static List<OrderTableDto> toOrderTablesDto(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}

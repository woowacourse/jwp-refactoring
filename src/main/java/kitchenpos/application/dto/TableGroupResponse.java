package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    private TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                toDtos(tableGroup.getOrderTables())
        );
    }

    private static List<OrderTableDto> toDtos(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableDto::of)
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

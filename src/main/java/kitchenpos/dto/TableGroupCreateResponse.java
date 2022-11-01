package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(final Long id, final LocalDateTime createdDate,
                                    final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupCreateResponse from(final TableGroup tableGroup) {
        return new TableGroupCreateResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getAllOrderTables()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

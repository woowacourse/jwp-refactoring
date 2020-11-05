package kitchenpos.builder;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupBuilder {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupBuilder() {
    }

    public TableGroupBuilder(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroupBuilder createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(id, createdDate, orderTables);
    }
}

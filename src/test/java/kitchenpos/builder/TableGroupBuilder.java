package kitchenpos.builder;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupBuilder {

    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroupBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroupBuilder createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TableGroupBuilder orderTables(OrderTables orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(id, createdDate, orderTables);
    }
}

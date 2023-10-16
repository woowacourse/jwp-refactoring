package fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupBuilder {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public static TableGroupBuilder init() {
        final TableGroupBuilder builder = new TableGroupBuilder();
        builder.id = null;
        builder.createdDate = LocalDateTime.now();
        builder.orderTables = List.of(OrderTableBuilder.init().id(1L).build(), OrderTableBuilder.init().id(2L).build());
        return builder;
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
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(this.id);
        tableGroup.setCreatedDate(this.createdDate);
        tableGroup.setOrderTables(this.orderTables);
        return tableGroup;
    }
}

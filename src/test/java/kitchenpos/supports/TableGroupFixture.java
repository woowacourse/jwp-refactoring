package kitchenpos.supports;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    private Long id = null;
    private LocalDateTime createdDate = LocalDateTime.now();
    private List<OrderTable> orderTables;

    private TableGroupFixture() {
    }

    public static TableGroupFixture fixture() {
        return new TableGroupFixture();
    }

    public TableGroupFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroupFixture createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TableGroupFixture orderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}

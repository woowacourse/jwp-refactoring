package kitchenpos.table.supports;

import java.util.List;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;

public class TableGroupFixture {

    private Long id = null;
    private List<OrderTable> orderTables = List.of(
        OrderTableFixture.fixture().id(1L).build(),
        OrderTableFixture.fixture().id(2L).build()
    );

    private TableGroupFixture() {
    }

    public static TableGroupFixture fixture() {
        return new TableGroupFixture();
    }

    public TableGroupFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroupFixture orderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(id, orderTables);
    }
}

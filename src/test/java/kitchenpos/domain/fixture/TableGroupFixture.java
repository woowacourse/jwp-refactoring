package kitchenpos.domain.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroupFixture() {
    }

    public static TableGroupFixture 테이블_그룹() {
        return new TableGroupFixture();
    }

    public TableGroupFixture 그룹화한_시간(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TableGroupFixture 주문_테이블들(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}

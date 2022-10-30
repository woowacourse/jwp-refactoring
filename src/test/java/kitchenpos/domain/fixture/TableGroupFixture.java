package kitchenpos.domain.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroupFixture() {
    }

    public static TableGroup 새로운_테이블_그룹() {
        return 테이블_그룹()
            .그룹화한_시간(LocalDateTime.now())
            .build();
    }

    public static TableGroup 테이블_그룹의_주문_테이블들은(final List<OrderTable> orderTables) {
        return 테이블_그룹()
            .주문_테이블들(orderTables)
            .그룹화한_시간(LocalDateTime.now())
            .build();
    }

    private static TableGroupFixture 테이블_그룹() {
        return new TableGroupFixture();
    }

    private TableGroupFixture 그룹화한_시간(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    private TableGroupFixture 주문_테이블들(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    private TableGroup build() {
        if (orderTables == null) {
            return new TableGroup(id, createdDate);
        }
        return new TableGroup(id, createdDate, orderTables);
    }
}

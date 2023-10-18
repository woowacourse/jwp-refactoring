package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹_생성(final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(orderTables, LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

}

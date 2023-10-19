package kitchenpos.fixture;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 테이블그룹_생성(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}

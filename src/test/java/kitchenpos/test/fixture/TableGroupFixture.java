package kitchenpos.test.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public static TableGroup 테이블_그룹(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}

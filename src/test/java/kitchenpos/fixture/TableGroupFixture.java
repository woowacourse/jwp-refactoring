package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup create(OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}

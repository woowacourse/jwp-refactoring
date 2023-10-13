package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    public static TableGroup createTableGroup(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}

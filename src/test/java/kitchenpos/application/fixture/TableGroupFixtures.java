package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    public static final TableGroup generateTableGroup(final List<OrderTable> orderTables) {
        return generateTableGroup(null, LocalDateTime.now(), orderTables);
    }

    public static final TableGroup generateTableGroup(final Long id, final TableGroup tableGroup) {
        return generateTableGroup(id, tableGroup.getCreatedDate(), tableGroup.getOrderTables());
    }

    public static final TableGroup generateTableGroup(final Long id,
                                                      final LocalDateTime createdDate,
                                                      final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}

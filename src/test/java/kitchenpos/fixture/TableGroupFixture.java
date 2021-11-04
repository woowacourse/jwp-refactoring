package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableGroupFixture {
    public static final TableGroup FIRST_SECOND_TABLE;
    public static final TableGroup FIRST_SECOND_TABLE_BEFORE_SAVE;

    static {
        FIRST_SECOND_TABLE = newInstance(1L, LocalDateTime.now(), OrderTableFixture.FIRST_EMPTY_FALSE, OrderTableFixture.SECOND_EMPTY_FALSE);
        FIRST_SECOND_TABLE_BEFORE_SAVE = newInstance(null, LocalDateTime.now(), OrderTableFixture.FIRST, OrderTableFixture.SECOND);
    }

    public static List<TableGroup> tableGroups() {
        return Collections.singletonList(FIRST_SECOND_TABLE);
    }

    private static TableGroup newInstance(Long id, LocalDateTime localDateTime, OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(localDateTime);
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        return tableGroup;
    }
}

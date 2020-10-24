package kitchenpos.fixture;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_4;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static final TableGroup TABLE_GROUP_FIXTURE_1 = new TableGroup();
    public static final TableGroup TABLE_GROUP_FIXTURE_2 = new TableGroup();

    static {
        TABLE_GROUP_FIXTURE_1.setCreatedDate(LocalDateTime.of(2010, 1, 1, 1, 1));
        TABLE_GROUP_FIXTURE_1.setOrderTables(Arrays.asList(ORDER_TABLE_FIXTURE_1, ORDER_TABLE_FIXTURE_2));
        TABLE_GROUP_FIXTURE_2.setCreatedDate(LocalDateTime.of(2010, 1, 1, 1, 1));
        TABLE_GROUP_FIXTURE_2.setOrderTables(Arrays.asList(ORDER_TABLE_FIXTURE_3, ORDER_TABLE_FIXTURE_4));
    }
}

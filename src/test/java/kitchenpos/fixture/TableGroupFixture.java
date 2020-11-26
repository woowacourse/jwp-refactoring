package kitchenpos.fixture;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_4;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_5;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_6;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static final TableGroup TABLE_GROUP_FIXTURE_1 = new TableGroup(LocalDateTime.of(2010, 1, 1, 1, 1),
        Arrays.asList(ORDER_TABLE_FIXTURE_1, ORDER_TABLE_FIXTURE_2, ORDER_TABLE_FIXTURE_3));
    public static final TableGroup TABLE_GROUP_FIXTURE_2 = new TableGroup(LocalDateTime.of(2010, 1, 1, 1, 1),
        Arrays.asList(ORDER_TABLE_FIXTURE_4, ORDER_TABLE_FIXTURE_5, ORDER_TABLE_FIXTURE_6));
}

package kitchenpos.fixture;

import static kitchenpos.fixture.OrderTableFixture.*;

import java.time.LocalDateTime;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static final TableGroup TABLE_GROUP = TestObjectUtils.createTableGroup(
            1L, LocalDateTime.now(), ORDER_TABLES);
}

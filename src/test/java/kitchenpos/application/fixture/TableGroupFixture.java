package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.OrderTableFixture.SAVED_ORDER_TABLE_EMPTY_FIRST;
import static kitchenpos.application.fixture.OrderTableFixture.SAVED_ORDER_TABLE_EMPTY_SECOND;
import static kitchenpos.application.fixture.OrderTableFixture.SAVED_ORDER_TABLE_NOT_EMPTY_FIRST;
import static kitchenpos.application.fixture.OrderTableFixture.SAVED_ORDER_TABLE_NOT_EMPTY_SECOND;

import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static final TableGroup UNSAVED_TABLE_GROUP_INVALID_INCLUDE_NOT_EMPTY_TABLE = new TableGroup(List.of(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST, SAVED_ORDER_TABLE_NOT_EMPTY_SECOND));
    public static final TableGroup UNSAVED_TABLE_GROUP_INVALID_TOO_LITTLE_TABLE = new TableGroup(List.of(SAVED_ORDER_TABLE_EMPTY_FIRST));
    public static final TableGroup UNSAVED_TABLE_GROUP = new TableGroup(List.of(SAVED_ORDER_TABLE_EMPTY_FIRST, SAVED_ORDER_TABLE_EMPTY_SECOND));

    public static final TableGroup SAVED_TABLE_GROUP = new TableGroup(List.of(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST, SAVED_ORDER_TABLE_NOT_EMPTY_SECOND));

    static {
        SAVED_TABLE_GROUP.setId(1L);
    }
}

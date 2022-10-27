package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture extends DomainCreator {

    public static TableGroup createTableGroup(final Long id, final OrderTable... orderTables) {
        return createTableGroup(id, List.of(orderTables));
    }
}

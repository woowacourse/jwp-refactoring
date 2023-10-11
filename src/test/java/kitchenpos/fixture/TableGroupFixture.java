package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }
}

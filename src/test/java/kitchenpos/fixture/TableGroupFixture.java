package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public final class TableGroupFixture {

    public static TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();

        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }

    private TableGroupFixture() {
    }
}

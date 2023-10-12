package kitchenpos.common.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정(Long tableGroupId) {
        return new TableGroup(tableGroupId, LocalDateTime.MAX, List.of());
    }

    public static TableGroup 단체_지정(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.MAX, orderTables);
    }

    public static TableGroup 단체_지정() {
        return new TableGroup(LocalDateTime.MAX, List.of());
    }
}

package kitchenpos;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;

public class TableGroupFixtures {

    private TableGroupFixtures() {
    }

    public static TableGroup create(final OrderTable orderTable1, final OrderTable orderTable2) {
        return new TableGroup(List.of(orderTable1, orderTable2));
    }
}

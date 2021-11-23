package kitchenpos;

import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup GROUP1 = new TableGroup(
            1L,
            LocalDateTime.now(),
            Arrays.asList(
                    new OrderTable(0, true),
                    new OrderTable(0, true)
            )
    );
}

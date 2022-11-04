package kitchenpos.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private final List<OrderTable> orderTables = List.of(OrderTable.of(0, true), OrderTable.of(1, true));


    @Test
    void 테이블_집합_생성() {
        Assertions.assertDoesNotThrow(() -> TableGroup.of(orderTables));
    }
}

package kitchenpos.tablegroup.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    void 단체_지정을_생성한다() {
        List<OrderTable> orderTables = TableFixtures.createOrderTables(true);

        assertDoesNotThrow(() -> new TableGroup());
    }
}

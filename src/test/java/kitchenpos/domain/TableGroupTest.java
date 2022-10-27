package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.exception.NotEnoughForGroupingException;
import kitchenpos.exception.OrderTableSizeException;
import kitchenpos.fixtures.OrderTableFixtures;
import kitchenpos.fixtures.TableGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("단체는 주문 테이블이 2개 이상이어야 한다")
    void validateSizeOfOrderTables() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(orderTable);

        assertThatThrownBy(() -> tableGroup.updateOrderTables(Collections.singletonList(orderTable)))
                .isExactlyInstanceOf(NotEnoughForGroupingException.class);
    }

    @Test
    @DisplayName("단체로 지정될 주문 테이블이 실제 존재해야한다")
    void validateExistOrderTable() {
        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(null, 2);
        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(orderTable1, orderTable2);

        assertThatThrownBy(() -> tableGroup.validateExistOrderTable(1))
                .isExactlyInstanceOf(OrderTableSizeException.class);
    }
}

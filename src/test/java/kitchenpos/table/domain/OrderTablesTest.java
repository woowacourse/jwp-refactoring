package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.table.exception.InvalidOrderTablesException;
import kitchenpos.table.exception.OrderTableNotEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTables 단위 테스트")
class OrderTablesTest {

    private static final Long TABLE_GROUP_ID = 1L;

    @DisplayName("OrderTable이 2개 미만일 경우 예외가 발생한다.")
    @Test
    void minException() {
        // given
        OrderTable orderTable = new OrderTable(5, true);

        // when, then
        assertThatThrownBy(() -> new OrderTables(Collections.singletonList(orderTable)))
            .isExactlyInstanceOf(InvalidOrderTablesException.class);
    }

    @DisplayName("OrderTable이 비어있는지 확인할 때")
    @Nested
    class Empty {

        @DisplayName("OrderTable이 비어있지 않을 경우 예외가 발생한다.")
        @Test
        void isNotEmptyException() {
            // given
            OrderTable orderTable1 = new OrderTable(5, true);
            OrderTable orderTable2 = new OrderTable(5, false);

            OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

            // when, then
            assertThatThrownBy(orderTables::validateEmpty)
                .isExactlyInstanceOf(OrderTableNotEmptyException.class);
        }

        @DisplayName("OrderTable이 tableGroup와 매핑되어있을 경우 예외가 발생한다.")
        @Test
        void isGroupedException() {
            // given
            OrderTable orderTable1 = new OrderTable(5, true);
            OrderTable orderTable2 = new OrderTable(5, true);
            orderTable2.groupBy(TABLE_GROUP_ID);

            OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

            // when, then
            assertThatThrownBy(orderTables::validateEmpty)
                .isExactlyInstanceOf(OrderTableNotEmptyException.class);
        }
    }

    @DisplayName("groupBy 기능을 이용하면 컬렉션 내의 모든 OrderTable들이 tableGroup과 매핑된다.")
    @Test
    void groupBy() {
        // given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(5, true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        // when
        for (OrderTable orderTable : orderTables.toList()) {
            assertThat(orderTable.isNotEmpty()).isFalse();
            assertThat(orderTable.isGrouped()).isFalse();
        }

        orderTables.groupBy(TABLE_GROUP_ID);

        // then
        for (OrderTable orderTable : orderTables.toList()) {
            assertThat(orderTable.isNotEmpty()).isTrue();
            assertThat(orderTable.isGrouped()).isTrue();
        }
    }
}

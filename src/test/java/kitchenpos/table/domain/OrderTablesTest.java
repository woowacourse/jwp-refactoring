package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderTablesTest {

    @Test
    @DisplayName("비어있다")
    void isEmpty() {
        OrderTables orderTables = new OrderTables(Collections.emptyList());

        assertThat(orderTables.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("크기가 더 작다.")
    void isSmallerThan() {
        OrderTables orderTables = new OrderTables(List.of(new OrderTable(10, true)));

        assertThat(orderTables.isSmallerThan(2)).isTrue();
    }

    @Test
    @DisplayName("value 중 어떤 것이든 사용 중이다.")
    void anyUsing() {
        OrderTables orderTables = new OrderTables(List.of(new OrderTable(10, true),
                new OrderTable(10, false)));

        assertThat(orderTables.anyUsing()).isTrue();
    }

    @Nested
    @DisplayName("그룹을 해제할 때")
    class UngroupTest {

        private OrderUngroupValidator orderUngroupValidator;

        @BeforeEach
        void setUp() {
            orderUngroupValidator = Mockito.mock(OrderUngroupValidator.class);
        }

        @Test
        @DisplayName("주문 상태가 Completion이 아니면 예외가 발생한다.")
        void notCompletionFailed() {
            OrderTables orderTables = new OrderTables(List.of(new OrderTable(10, true),
                    new OrderTable(10, false)));

            doThrow(IllegalArgumentException.class).when(orderUngroupValidator).validateOrderStatus(anyList());
            assertThatThrownBy(() -> orderTables.ungroup(orderUngroupValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹을 해제한다.")
        void ungroup() {
            OrderTable orderTable1 = new OrderTable(1L, 10, true);
            OrderTable orderTable2 = new OrderTable(1L, 5, true);
            OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

            doNothing().when(orderUngroupValidator).validateOrderStatus(anyList());
            orderTables.ungroup(orderUngroupValidator);

            assertAll(
                    () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable1.isEmpty()).isFalse(),
                    () -> assertThat(orderTable2.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable2.isEmpty()).isFalse()
            );
        }
    }
}

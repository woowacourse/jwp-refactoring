package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidTableGroupException;
import kitchenpos.table.infrastructure.OrderUngroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TableGroupTest {

    @Nested
    @DisplayName("TableGroup을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("OrderTable이 비어있을 경우 예외가 발생한다.")
        void orderTableEmptyFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Collections.emptyList()))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }

        @Test
        @DisplayName("OrderTable이 기본 수보다 적을 경우 예외가 발생한다.")
        void orderTableSizeFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(new OrderTable(10, true))))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }


        @Test
        @DisplayName("OrderTable이 이미 사용 중이면 예외가 발생한다.")
        void orderTableAnyUsingFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(),
                    List.of(new OrderTable(10, false), new OrderTable(10, true))))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("주문 테이블이 이미 사용 중입니다.");
        }
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
                    new OrderTable(10, true)));
            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

            doThrow(IllegalArgumentException.class).when(orderUngroupValidator).validateOrderStatus(anyList());
            assertThatThrownBy(() -> tableGroup.ungroup(orderUngroupValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹을 해제한다.")
        void ungroup() {
            OrderTable orderTable1 = new OrderTable(10, true);
            OrderTable orderTable2 = new OrderTable(5, true);
            OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

            doNothing().when(orderUngroupValidator).validateOrderStatus(anyList());
            tableGroup.ungroup(orderUngroupValidator);

            assertAll(
                    () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable1.isEmpty()).isFalse(),
                    () -> assertThat(orderTable2.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable2.isEmpty()).isFalse()
            );
        }
    }
}

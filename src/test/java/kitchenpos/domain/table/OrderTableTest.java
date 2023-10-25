package kitchenpos.domain.table;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderFixture.주문;
import static kitchenpos.domain.order.OrderLineItemFixture.주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.order.Order;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 주문_테이블에_주문을_추가한다() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);
        Order order = Order.of(orderTable, List.of(주문_항목(1L, 1L)), 1);

        // when
        orderTable.add(order);

        // then
        assertThat(orderTable.getOrders()).contains(order);
    }

    @Test
    void 주문_테이블이_주문을_포함하는지_확인한다() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);
        Order order = Order.of(orderTable, List.of(주문_항목(1L, 1L)), 1);
        orderTable.add(order);

        // when
        boolean actual = orderTable.contains(order);

        // then
        assertThat(actual).isTrue();
    }

    @Nested
    class 주문_테이블이_비어있는지_여부를_변경할_때 {

        @Test
        void 단체_지정된_주문_테이블이라면_예외를_던진다() {
            // given
            OrderTable groupedOrderTable = new OrderTable(null, 0, true);
            Long tableGroupId = 1L;
            groupedOrderTable.group(tableGroupId);

            boolean isEmpty = false;

            // expect
            assertThatThrownBy(() -> groupedOrderTable.changeIsEmpty(isEmpty))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정된 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        @Test
        void 조리_혹은_식사_중인_주문이_존재하는_주문_테이블이라면_예외를_던진다() {
            // given
            OrderTable orderTable = new OrderTable(null, 0, false);
            orderTable.add(주문(COOKING));

            boolean isEmpty = false;

            // expect
            assertThatThrownBy(() -> orderTable.changeIsEmpty(isEmpty))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조리 혹은 식사 중인 주문이 존재하는 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @CsvSource({"true, false", "false, true"})
        void 정상적으로_변경한다(boolean original, boolean expected) {
            // given
            Long tableGroupId = null;
            OrderTable orderTable = new OrderTable(tableGroupId, 0, original);

            // when
            orderTable.changeIsEmpty(expected);

            // then
            assertThat(orderTable.isEmpty()).isEqualTo(expected);
        }
    }

    @Nested
    class 주문_테이블의_손님_수를_변경할_때 {

        @Test
        void 손님_수가_0_미만이면_예외를_던진다() {
            // given
            int invalidNumberOfGuests = -1;
            OrderTable orderTable = new OrderTable(null, 1, false);

            // expect
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 0 미만일 수 없습니다.");
        }

        @Test
        void 빈_주문_테이블이라면_예외를_던진다() {
            // given
            OrderTable orderTable = new OrderTable(null, 0, true);

            // expect
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 주문 테이블에 손님 수를 변경할 수 없습니다.");
        }
    }

    @ParameterizedTest
    @CsvSource({"true, false", "false, true"})
    void 주문_테이블이_찼는지_확인한다(boolean isEmpty, boolean expected) {
        // given
        OrderTable orderTable = new OrderTable(null, 0, isEmpty);

        // when
        boolean actual = orderTable.isFilled();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_테이블이_단체_지정되었는지_확인한다() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(tableGroupId, 0, true);

        // when
        boolean actual = orderTable.isGrouped();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주문_테이블을_단체_지정한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);
        Long tableGroupId = 1L;

        // when
        orderTable.group(tableGroupId);

        // then
        assertThat(orderTable.isGrouped()).isTrue();
    }

    @Test
    void 주문_테이블을_단체_지정_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);
        Long tableGroupId = 1L;
        orderTable.group(tableGroupId);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.isGrouped()).isFalse();
    }

    @Nested
    class 주문_테이블이_주문을_받을_수_있는지_검증할_때 {

        @Test
        void 빈_주문_테이블이면_예외를_던진다() {
            // given
            boolean isEmpty = true;
            OrderTable emptyOrderTable = new OrderTable(null, 0, isEmpty);

            // expect
            assertThatThrownBy(emptyOrderTable::validateTableCanTakeOrder)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 주문 테이블은 주문을 받을 수 없습니다");
        }

        @Test
        void 채워진_주문_테이블은_예외를_던지지_않는다() {
            // given
            boolean isEmpty = false;
            OrderTable filledOrderTable = new OrderTable(null, 0, isEmpty);

            // expect
            assertThatNoException().isThrownBy(filledOrderTable::validateTableCanTakeOrder);
        }
    }
}

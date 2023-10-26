package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderTableFixture.단체_지정_빈_주문_테이블;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Nested
    class 주문_테이블이_비어있는지_여부를_변경할_때 {

        @Test
        void 단체_지정된_주문_테이블이라면_예외를_던진다() {
            // given
            Long tableGroupId = 1L;
            OrderTable groupedOrderTable = 단체_지정_빈_주문_테이블(tableGroupId);

            boolean isEmpty = true;

            // expect
            assertThatThrownBy(() -> groupedOrderTable.changeIsEmpty(isEmpty))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정된 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @CsvSource({"true, false, false", "false, true, true"})
        void 정상적으로_변경한다(boolean originalIsEmpty, boolean isEmpty, boolean expected) {
            // given
            OrderTable orderTable = 단체_지정_없는_주문_테이블(originalIsEmpty);

            // when
            orderTable.changeIsEmpty(isEmpty);

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

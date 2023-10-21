package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
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
            OrderTable groupedOrderTable = new OrderTable(tableGroupId, 0, true);

            boolean hasCookingOrMealOrder = false;
            boolean isEmpty = false;

            // expect
            assertThatThrownBy(() -> groupedOrderTable.changeIsEmpty(hasCookingOrMealOrder, isEmpty))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정된 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        @Test
        void 조리_혹은_식사_중인_주문이_존재하는_주문_테이블이라면_예외를_던진다() {
            // given
            OrderTable orderTable = new OrderTable(0, false);

            boolean hasCookingOrMealOrder = true;
            boolean isEmpty = false;

            // expect
            assertThatThrownBy(() -> orderTable.changeIsEmpty(hasCookingOrMealOrder, isEmpty))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조리 혹은 식사 중인 주문이 존재하는 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @CsvSource({"true, false", "false, true"})
        void 정상적으로_변경한다(boolean original, boolean expected) {
            // given
            Long tableGroupId = null;
            OrderTable orderTable = new OrderTable(tableGroupId, 0, original);
            boolean hasCookingOrMealOrder = false;

            // when
            orderTable.changeIsEmpty(hasCookingOrMealOrder, expected);

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
            OrderTable orderTable = new OrderTable(1, false);

            // expect
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 0 미만일 수 없습니다.");
        }

        @Test
        void 빈_주문_테이블이라면_예외를_던진다() {
            // given
            OrderTable orderTable = new OrderTable(0, true);

            // expect
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 주문 테이블에 손님 수를 변경할 수 없습니다.");
        }
    }
}

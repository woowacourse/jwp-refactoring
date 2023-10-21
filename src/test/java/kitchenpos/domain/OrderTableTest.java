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
            OrderTable groupedOrderTable = new OrderTable(0, true);
            groupedOrderTable.group(tableGroupId);

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

    @ParameterizedTest
    @CsvSource({"true, false", "false, true"})
    void 주문_테이블이_찼는지_확인한다(boolean isEmpty, boolean expected) {
        // given
        OrderTable orderTable = new OrderTable(0, isEmpty);

        // when
        boolean actual = orderTable.isFilled();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"1, true", ", false"})
    void 주문_테이블이_단체_지정되었는지_확인한다(Long tableGroupId, boolean expected) {
        // given
        OrderTable orderTable = new OrderTable(tableGroupId, 0, true);

        // when
        boolean actual = orderTable.isGrouped();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 주문_테이블을_단체_지정한다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        Long tableGroupId = 1L;

        // when
        orderTable.group(tableGroupId);

        // then
        assertThat(orderTable.isGrouped()).isTrue();
    }
    
    @Test
    void 주문_테이블을_단체_지정_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        Long tableGroupId = 1L;
        orderTable.group(tableGroupId);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.isGrouped()).isFalse();
    }
}

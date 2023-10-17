package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.domain.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블(OrderTable) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    @Nested
    class 그룹화_시 {

        @Test
        void 비어있지_않은_상태가_된다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);

            // when
            orderTable.grouping(1L);

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Nested
    class 언그룹_시 {

        @Test
        void 테이블_그룹_아이디가_사라진다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);
            orderTable.grouping(1L);

            // when
            orderTable.ungroup();

            // then
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }

    @Nested
    class 손님_수_변경_시 {

        @Test
        void 변경할_손님_수가_0명_미만이라면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setNumberOfGuests(-1)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("손님 수는 0명 이상이어야 합니다.");
        }

        @Test
        void 테이블이_비어있으면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, true);

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setNumberOfGuests(11)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("비어있지 않은 테이블의 경우 손님 수를 변경할 수 없습니다.");
        }

        @Test
        void 테이블이_비어있지_않으며_변경할_손님_수가_0_이상이라면_변경_가능() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertDoesNotThrow(() ->
                    orderTable.setNumberOfGuests(11)
            );
        }
    }
}

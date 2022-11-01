package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블 생성자")
    @Nested
    class Constructor {

        @DisplayName("방문한 손님 수가 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_NumberOfGuestsIsLessThan0() {
            // given & when & then
            assertThatThrownBy(() -> new OrderTable(-1, false))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("방문한 손님 수 변경 메소드")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("방문한 손님 수가 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_NumberOfGuestsIsLessThan0() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

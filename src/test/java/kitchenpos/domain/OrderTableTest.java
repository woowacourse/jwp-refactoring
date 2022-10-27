package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Nested
    @DisplayName("OrderTable이 사용 중인지 확인할 때 ")
    class IsUsingTest {

        @Test
        @DisplayName("비어있지 않을 경우 사용 중이다.")
        void emptyUsing() {
            OrderTable orderTable = new OrderTable(10, false);

            assertThat(orderTable.isUsing()).isTrue();
        }

        @Test
        @DisplayName("tableGroupId가 있을 경우 사용 중이다.")
        void hasTableUsing() {
            OrderTable orderTable = new OrderTable(1L, 10, false);

            assertThat(orderTable.isUsing()).isTrue();
        }

        @Test
        @DisplayName("사용 중이지 않다.")
        void isNotUsing() {
            OrderTable orderTable = new OrderTable(10, true);

            assertThat(orderTable.isUsing()).isFalse();
        }
    }

    @Nested
    @DisplayName("OrderTable의 비어있는 여부를 변경할 때 ")
    class ChangeEmptyTest {

        @Test
        @DisplayName("이미 테이블 그룹에 속해있으면 예외가 발생한다.")
        void alreadyJoinedFailed() {
            OrderTable orderTable = new OrderTable(1L, 10, true);

            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                    .isInstanceOf(InvalidOrderTableException.class)
                    .hasMessage("이미 테이블 그룹에 속해있습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void changeEmpty() {
            OrderTable orderTable = new OrderTable(10, true);
            orderTable.changeEmpty(false);

            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("OrderTable의 정원을 변경할 때 ")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("변경할 정원이 음수일 경우 예외가 발생한다.")
        void numberNegativeFailed() {
            OrderTable orderTable = new OrderTable(1L, 10, true);

            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                    .isInstanceOf(InvalidOrderTableException.class)
                    .hasMessage("테이블 인원은 음수일 수 없습니다.");
        }

        @Test
        @DisplayName("비어있을 경우 예외가 발생한다.")
        void emptyFailed() {
            OrderTable orderTable = new OrderTable(10, true);

            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                    .isInstanceOf(InvalidOrderTableException.class)
                    .hasMessage("테이블이 비어있을 수 없습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void changeNumberOfGuests() {
            OrderTable orderTable = new OrderTable(10, false);
            orderTable.changeNumberOfGuests(5);

            assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
        }
    }
}

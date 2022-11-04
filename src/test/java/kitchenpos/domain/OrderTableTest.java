package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 클래스의")
class OrderTableTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuests_ExceptionThrown() {
            assertThatThrownBy(() -> new OrderTable(-1, false))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateNumberOfGuests 메서드는")
    class UpdateNumberOfGuests {

        @Test
        @DisplayName("테이블 인원 수를 업데이트한다.")
        void success() {
            final OrderTable orderTable = new OrderTable(1, false);
            orderTable.updateNumberOfGuests(2);
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuests_ExceptionThrown() {
            final OrderTable orderTable = new OrderTable(1, false);
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("빈 테이블인 경우 예외를 던진다.")
        void empty_ExceptionThrown() {
            final OrderTable orderTable = new OrderTable(0, true);
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}


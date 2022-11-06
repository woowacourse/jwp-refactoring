package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 은 ")
class OrderTableTest {

    @DisplayName("테이블 그룹에 속한 주문 테이블 인지를 판별한다.")
    @Test
    void isPartOfTableGroup() {
        final OrderTable orderTable = new OrderTable(0, true);

        assertThat(orderTable.isPartOfTableGroup()).isFalse();
    }

    @DisplayName("주문 테이블그룹에 속한 게스트 인원수를 변경할 때")
    @Nested
    class OrderTableGuestNumberAlternationTest {

        @DisplayName("인원수를 변경할 수 있으면 변경한다.")
        @Test
        void changeNumberOfGuestSuccess() {
            final OrderTable orderTable = new OrderTable(3, false);

            assertThat(orderTable.changeNumberOfGuest(1).getNumberOfGuests()).isEqualTo(1);
        }

        @DisplayName("게스트 인원 수가 0명 미만이거나 테이블이 비어있으면 예외를 던진다.")
        @Test
        void changeNumberOfGuestFail() {
            final OrderTable orderTable = new OrderTable(1, false);
            assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                    .isInstanceOf(IllegalArgumentException.class);

        }
    }

}

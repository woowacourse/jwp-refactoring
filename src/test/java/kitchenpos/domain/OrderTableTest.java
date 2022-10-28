package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("빈 주문테이블인 경우 예외를 발생한다.")
    @Test
    void isEmptyTrue() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(orderTable::validateEmpty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("빈 주문테이블이 아닌 경우 예외를 발생하지 않는다.")
    @Test
    void isEmptyFalse() {
        // given
        OrderTable orderTable = new OrderTable(2, false);

        // when & then
        assertDoesNotThrow(orderTable::validateEmpty);
    }

}

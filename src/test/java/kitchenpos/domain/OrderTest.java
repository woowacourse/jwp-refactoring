package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void Order_객체를_생성한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1L, 1, false);

        // when & then
        assertThatCode(() -> Order.from(orderTable))
                .doesNotThrowAnyException();
    }

    @Test
    void OrderTable이_빈_상태이면_Order_객체를_생성할_수_없다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1L, 1, true);

        // when & then
        assertThatThrownBy(() -> Order.from(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

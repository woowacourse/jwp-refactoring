package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문의_상태가_완료_상태가_아닌지_확인한다() {
        // given
        final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now());

        // when
        final boolean actual = order.isCompletion();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 주문의_상태가_완료_상태가_맞는지_확인한다() {
        // given
        final Order order = new Order(null, OrderStatus.COMPLETION, LocalDateTime.now());

        // when
        final boolean actual = order.isCompletion();

        // then
        assertThat(actual).isTrue();
    }
}

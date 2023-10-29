package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문이_완료된_상태인지_확인할_수_있다() {
        //given
        Order 결제완료_주문 = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now());

        //when
        boolean result = 결제완료_주문.isOrderCompletion();

        //then
        assertThat(result).isTrue();
    }
}

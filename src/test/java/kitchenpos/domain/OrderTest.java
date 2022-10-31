package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문항목이_존재하지_않으면_예외를_반환한다() {
        // given, when, then
        assertThatThrownBy(() -> new Order(null, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

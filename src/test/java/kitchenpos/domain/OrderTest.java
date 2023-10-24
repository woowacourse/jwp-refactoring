package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_목록이_비어있으면_예외() {
        // when & then
        Assertions.assertThatThrownBy(() -> Order.builder()
                        .id(1L)
                        .orderLineItems(Collections.emptyList())
                        .orderStatus(OrderStatus.COOKING.name())
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_목록이_비어있으면_예외() {
        // when & then
        assertThatThrownBy(() -> Order.builder()
                .id(1L)
                .orderLineItems(Collections.emptyList())
                .orderStatus(OrderStatus.COOKING)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}

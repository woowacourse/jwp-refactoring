package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 중복_OrderLineItem_추기_시_예외() {
        // given
        Order order = new Order(1L, null, null, null, new ArrayList<>());
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, OrderFixture.builder().withId(1L).build(), 10L);
        order.addOrderLineItem(orderLineItem1);

        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, OrderFixture.builder().withId(1L).build(), 5L);

        // when && then
        assertThatThrownBy(() -> order.addOrderLineItem(orderLineItem2))
            .isInstanceOf(IllegalArgumentException.class);
    }

}

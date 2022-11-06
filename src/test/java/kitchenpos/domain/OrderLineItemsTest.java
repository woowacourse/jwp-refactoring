package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void orderLineItem이_null이면_OrderLineItems_객체를_생성할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = null;

        // when & then
        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void orderLineItem이_빈_리스트면_OrderLineItems_객체를_생성할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when & then
        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

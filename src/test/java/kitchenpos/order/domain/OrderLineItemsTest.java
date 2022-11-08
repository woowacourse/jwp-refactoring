package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    @Test
    @DisplayName("주문 메뉴가 올바른지 확인한다")
    void hasValidMenus() {
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());

        final boolean actual = orderLineItems.hasValidMenus(0);

        assertThat(actual).isTrue();
    }
}

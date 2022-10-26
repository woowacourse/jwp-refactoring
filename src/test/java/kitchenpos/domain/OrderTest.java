package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.fixtures.OrderFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 항목은 비어있을 수 없다")
    void validateNotEmptyOrderLineItems() {
        final Order order = OrderFixtures.COOKING_ORDER.create();

        assertThatThrownBy(order::validateNotEmptyOrderLineItems)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴 개수와 실제 존재하는 메뉴 개수는 일치해야 한다")
    void validateExistMenu() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(1L, orderLineItem);

        assertThatThrownBy(() -> order.validateExistMenu(2))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태가 완료가 아닌지 검증한다")
    void validateOrderNotCompletion() {
        final Order order = OrderFixtures.COMPLETION_ORDER.create();

        assertThatThrownBy(order::validateOrderNotCompletion)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}

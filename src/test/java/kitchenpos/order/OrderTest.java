package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.OrderCompletionException;
import kitchenpos.exception.OrderLineItemEmptyException;
import kitchenpos.exception.OrderLineItemSizeException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.support.fixtures.OrderFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 항목은 비어있을 수 없다")
    void validateNotEmptyOrderLineItems() {
        assertThatThrownBy(OrderFixtures.COOKING_ORDER::create)
                .isExactlyInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴 개수와 실제 존재하는 메뉴 개수는 일치해야 한다")
    void validateExistMenu() {
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, 1L, 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(1L, orderLineItem);

        assertThatThrownBy(() -> order.validateOrderLineItemSize(2))
                .isExactlyInstanceOf(OrderLineItemSizeException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태라면 상태를 변경할 수 없다")
    void validateOrderNotCompletion() {
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, 1L, 2);
        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderLineItems(orderLineItem);

        assertThatThrownBy(() -> order.updateOrderStatus(OrderFixtures.COOKING_ORDER.name()))
                .isExactlyInstanceOf(OrderCompletionException.class);
    }
}

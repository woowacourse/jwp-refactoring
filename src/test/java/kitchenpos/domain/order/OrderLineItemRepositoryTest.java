package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("주문 id로 모든 주문 항목을 불러온다.")
    void findAllByOrderId() {
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order = Order.create(orderTable1.getId(), List.of(orderLineItem));
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());
        final OrderLineItem findOrderLineItem = orderLineItems.get(0);
        assertAll(
                () -> assertThat(findOrderLineItem.getOrderId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(findOrderLineItem.getMenuId()).isEqualTo(menu.getId()),
                () -> assertThat(findOrderLineItem.getQuantity()).isEqualTo(1)
        );
    }
}

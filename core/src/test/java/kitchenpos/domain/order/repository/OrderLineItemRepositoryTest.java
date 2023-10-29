package kitchenpos.domain.order.repository;

import kitchenpos.domain.RepositoryTestConfig;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderLineItems;
import kitchenpos.order.OrderMenu;
import kitchenpos.order.repository.OrderLineItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Test
    void findAllByOrderId() {
        // given
        final Long orderTableId1 = 1L;
        final Long orderTableId2 = 2L;

        final long savedMenuId = 1L;
        final OrderMenu wooDongOrderMenu = createOrderMenu(savedMenuId, "우동", BigDecimal.valueOf(5000));
        final Order firstOrder = createOrder(orderTableId1, COOKING, LocalDateTime.now());
        final Long savedFirstOrderId = 1L;
        final OrderLineItem expected = createOrderLineItem(savedFirstOrderId, wooDongOrderMenu, 1);
        firstOrder.addAllOrderLineItems(OrderLineItems.from(List.of(expected)));

        final OrderMenu sushiOrderMenu = createOrderMenu(savedMenuId, "초밥", BigDecimal.valueOf(15000));
        final Order secondOrder = createOrder(orderTableId2, COOKING, LocalDateTime.now());
        final Long savedSecondOrderId = 2L;
        final OrderLineItem otherOrderLineItem = createOrderLineItem(savedSecondOrderId, sushiOrderMenu, 1);
        secondOrder.addAllOrderLineItems(OrderLineItems.from(List.of(otherOrderLineItem)));

        em.flush();
        em.close();

        // when
        final List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(firstOrder.getId());

        // then
        assertThat(actual).containsExactly(expected);
    }
}

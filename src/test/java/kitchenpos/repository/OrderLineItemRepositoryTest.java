package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Test
    void findAllByOrderId() {
        // given
        final OrderTable orderTable1 = createOrderTable(null, 10, true);
        final OrderTable orderTable2 = createOrderTable(null, 5, true);

        final MenuGroup japanese = createMenuGroup("일식");
        final Menu wooDong = createMenu("우동", BigDecimal.valueOf(5000), japanese);
        final Order firstOrder = createOrder(orderTable1, COOKING, LocalDateTime.now());
        final OrderLineItem expected = createOrderLineItem(firstOrder, wooDong, 1);
        firstOrder.addAllOrderLineItems(OrderLineItems.from(List.of(expected)));

        final Menu sushi = createMenu("초밥", BigDecimal.valueOf(15000), japanese);
        final Order secondOrder = createOrder(orderTable2, COOKING, LocalDateTime.now());
        final OrderLineItem otherOrderLineItem = createOrderLineItem(secondOrder, sushi, 1);
        secondOrder.addAllOrderLineItems(OrderLineItems.from(List.of(otherOrderLineItem)));

        em.flush();
        em.close();

        // when
        final List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(firstOrder.getId());

        // then
        assertThat(actual).containsExactly(expected);
    }
}

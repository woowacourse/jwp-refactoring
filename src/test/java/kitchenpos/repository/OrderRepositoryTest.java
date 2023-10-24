package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        final OrderTable orderTable = createOrderTable(null, 10, false);

        final MenuGroup japanese = createMenuGroup("일식");
        final Menu wooDong = createMenu("우동", BigDecimal.valueOf(5000), japanese);
        final Order firstOrder = createOrder(orderTable, MEAL, LocalDateTime.now());
        final OrderLineItem expected = createOrderLineItem(firstOrder, wooDong, 1);
        firstOrder.addAllOrderLineItems(List.of(expected));

        final Menu sushi = createMenu("초밥", BigDecimal.valueOf(15000), japanese);
        final Order secondOrder = createOrder(orderTable, COOKING, LocalDateTime.now());
        final OrderLineItem otherOrderLineItem = createOrderLineItem(secondOrder, sushi, 1);
        secondOrder.addAllOrderLineItems(List.of(otherOrderLineItem));

        em.flush();
        em.close();

        // when
        final boolean orderStatusMeal = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(MEAL));
        final boolean orderStatusCooking = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(COOKING));
        final boolean orderStatusCompletion = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(COMPLETION));

        // then
        assertAll(
                () -> assertThat(orderStatusCompletion).isFalse(),
                () -> assertThat(orderStatusMeal).isTrue(),
                () -> assertThat(orderStatusCooking).isTrue()
        );
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        final OrderTable orderTable = createOrderTable(null, 10, false);

        final MenuGroup japanese = createMenuGroup("일식");
        final Menu wooDong = createMenu("우동", BigDecimal.valueOf(5000), japanese);
        final Order firstOrder = createOrder(orderTable, MEAL, LocalDateTime.now());
        final OrderLineItem expected = createOrderLineItem(firstOrder, wooDong, 1);
        firstOrder.addAllOrderLineItems(List.of(expected));

        final Menu sushi = createMenu("초밥", BigDecimal.valueOf(15000), japanese);
        final Order secondOrder = createOrder(orderTable, COOKING, LocalDateTime.now());
        final OrderLineItem otherOrderLineItem = createOrderLineItem(secondOrder, sushi, 1);
        secondOrder.addAllOrderLineItems(List.of(otherOrderLineItem));

        em.flush();
        em.close();

        // when
        final boolean orderStatusMeal = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable.getId()), List.of(MEAL));
        final boolean orderStatusCooking = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable.getId()), List.of(COOKING));
        final boolean orderStatusCompletion = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable.getId()), List.of(COMPLETION));

        // then
        assertAll(
                () -> assertThat(orderStatusCompletion).isFalse(),
                () -> assertThat(orderStatusMeal).isTrue(),
                () -> assertThat(orderStatusCooking).isTrue()
        );
    }
}

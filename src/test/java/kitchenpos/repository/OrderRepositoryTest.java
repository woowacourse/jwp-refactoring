package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        final TableGroup tableGroup = createTableGroup(LocalDateTime.now());
        final OrderTable orderTable = createOrderTable(tableGroup, 10, false);
        tableGroup.addOrderTables(List.of(orderTable));

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
        final TableGroup tableGroup = createTableGroup(LocalDateTime.now());
        final OrderTable orderTable = createOrderTable(tableGroup, 10, false);
        tableGroup.addOrderTables(List.of(orderTable));

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

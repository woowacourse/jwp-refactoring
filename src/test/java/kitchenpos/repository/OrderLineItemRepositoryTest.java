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

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Test
    void findAllByOrderId() {
        // given
        final TableGroup tableGroup1 = createTableGroup(LocalDateTime.now());
        final TableGroup tableGroup2 = createTableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = createOrderTable(tableGroup1, 10, false);
        final OrderTable orderTable2 = createOrderTable(tableGroup2, 5, false);

        tableGroup1.addOrderTables(List.of(orderTable1));
        tableGroup2.addOrderTables(List.of(orderTable2));

        final MenuGroup japanese = createMenuGroup("일식");
        final Menu wooDong = createMenu("우동", BigDecimal.valueOf(5000), japanese);
        final Order firstOrder = createOrder(orderTable1, COOKING, LocalDateTime.now());
        final OrderLineItem expected = createOrderLineItem(firstOrder, wooDong, 1);
        firstOrder.addAllOrderLineItems(List.of(expected));

        final Menu sushi = createMenu("초밥", BigDecimal.valueOf(15000), japanese);
        final Order secondOrder = createOrder(orderTable2, COOKING, LocalDateTime.now());
        final OrderLineItem otherOrderLineItem = createOrderLineItem(secondOrder, sushi, 1);
        secondOrder.addAllOrderLineItems(List.of(otherOrderLineItem));

        em.flush();
        em.close();

        // when
        final List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(firstOrder.getId());

        // then
        assertThat(actual).containsExactly(expected);
    }
}

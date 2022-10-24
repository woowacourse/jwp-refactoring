package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("OrderLineItems이 비어있는 경우 예외가 발생한다.")
    @Test
    void create_orderLineItemsEmpty_throwsException() {
        // given
        final Order order = new Order(9L, OrderStatus.COOKING.name(), Collections.emptyList());

        // when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem의 menuId가 존재하지 않는 id면 예외가 발생한다.")
    @Test
    void create_notExistMenu_throwsException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(8L, 3);
        final Order order = new Order(9L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem의 OrderTable이 empty이면 예외가 발생한다.")
    @Test
    void create_alreadyExistOrderTable_throwsException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order를 등록한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), Arrays.asList(orderLineItem));

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("Order를 조회한다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), Arrays.asList(orderLineItem));
        final Order savedOrder = orderService.create(order);
        final List<Order> expected = Arrays.asList(savedOrder);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문을_생성한다() {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);

        assertThat(orderDao.findById(savedOrder.getId())).isPresent();
    }

    @Test
    void 주문을_생성할때_orderLineItems가_비었으면_예외를_발생한다() {
        Order order = new Order();
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_메뉴의수가_다르면_예외를_발생한다() {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_orderTableId가_존재하지않으면_예외를_발생한다() {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(0L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        int beforeSize = orderService.list().size();
        orderService.create(order);

        assertThat(orderService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문_상태를_변경한다() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(1L, order);

        assertThat(orderDao.findById(savedOrder.getId()).get().getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_상태를_변경할때_유효하지않은_아이디면_예외를_반환한다() {
        Order order = new Order();

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경할때_완료상태면_예외를_반환한다() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(false));
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem createOrderLineItem(Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        return orderLineItem;
    }

    private OrderTable createOrderTable(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}

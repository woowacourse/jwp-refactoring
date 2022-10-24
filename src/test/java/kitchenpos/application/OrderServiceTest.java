package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() {
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 10);
        OrderTable orderTable = OrderTable.of(1L, null, 1, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = Order.of(savedOrderTable.getId(), "", LocalDateTime.now(), List.of(orderLineItem));
        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionWhenNotCollectOrderLineItems() {
        OrderLineItem orderLineItem = OrderLineItem.of(0L, 0L, 10);
        Order order = Order.of(1L, "", LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴가 포함되어 있습니다.");
    }

    @Test
    void createThrowExceptionWhenEmptyOrderTable() {
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 10);
        Order order = Order.of(null, "", LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블을 입력해야 합니다.");
    }

    @Test
    void createThrowExceptionWhenEmptyTable() {
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 10);
        Order order = Order.of(1L, "", LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있습니다.");
    }

    @Test
    void list() {
        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(0);
    }

    @Test
    void changeOrderStatus() {
        Order order = Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        Long savedId = orderDao.save(order)
                .getId();

        Order changeOrder = Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        Order changedOrder = orderService.changeOrderStatus(savedId, changeOrder);

        assertThat(changedOrder.getOrderedTime()).isEqualTo(changedOrder.getOrderedTime());
    }

    @Test
    void changeOrderStatusThrowExceptionWhenNotExistOrder() {
        Order order = Order.of(0L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("order가 존재하지 않습니다.");
    }

    @Test
    void changeOrderStatusThrowExceptionWhenAlreadyCompleteOrder() {
        Order order = Order.of(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), new ArrayList<>());
        Long savedId = orderDao.save(order)
                .getId();

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedId, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 이미 완료되었습니다.");
    }
}

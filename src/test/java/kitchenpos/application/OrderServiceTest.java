package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);

        Order order = orderService.create(new Order(orderTable.getId(), createOrderLineItem()));

        assertThat(order).isNotNull();
    }

    @DisplayName("주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderLineItem() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);

        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목의 메뉴에 등록되어 있지 않은 주문 항목이 있으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderLineItem() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);

        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), createInvalidOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에서의 주문 테이블이 존재하지 않는 주문 테이블일 경우 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        assertThatThrownBy(
                () -> orderService.create(new Order(9999L, createOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에서의 주문 테이블이 주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTable() {
        assertThatThrownBy(
                () -> orderService.create(new Order(1L, createOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);
        orderService.create(new Order(orderTable.getId(), createOrderLineItem()));

        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1L);
    }

    @DisplayName("주문의 상태를 수정할 수 있다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);
        Order order = orderService.create(new Order(orderTable.getId(), createOrderLineItem()));
        Order changeOrder = new Order();
        changeOrder.setOrderStatus("MEAL");

        orderService.changeOrderStatus(order.getId(), changeOrder);
        Order foundOrder = orderDao.findById(order.getId()).get();

        assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("존재하지 않는 주문일 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithInvalidOrder() {
        Order changeOrder = new Order();
        changeOrder.setOrderStatus("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(9999L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 계산 완료인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithCompletionOrderStatus() {
        OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        orderTableDao.save(orderTable);
        Order order = orderService.create(new Order(orderTable.getId(), createOrderLineItem()));
        Order changeOrder = new Order();
        changeOrder.setOrderStatus("COMPLETION");
        orderService.changeOrderStatus(order.getId(), changeOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderLineItem> createOrderLineItem() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 10));
        return orderLineItems;
    }

    private List<OrderLineItem> createInvalidOrderLineItem() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(9999L, 10));
        return orderLineItems;
    }
}

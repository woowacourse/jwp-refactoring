package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    private Order order;

    @BeforeEach
    void setUp() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        final OrderTable orderTable = orderTableDao.findById(1L).get();
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        order = new Order();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(1L);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createTest() {
        // given

        // when
        final Order expect = orderService.create(order);

        // then
        final Order actual = orderDao.findById(expect.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void listTest() {
        // given
        final List<Order> preSavedOrder = orderDao.findAll();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        orderDao.save(order);

        final List<Order> expect = preSavedOrder;
        expect.add(order);

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 상태를 업데이트한다.")
    void changeOrderStatusTest() {
        // given
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        // when
        final String expectStatus = OrderStatus.MEAL.name();
        order.setOrderStatus(expectStatus);

        final Order actual = orderService.changeOrderStatus(savedOrder.getId(), order);

        // then
        assertEquals(expectStatus, actual.getOrderStatus());
    }
}

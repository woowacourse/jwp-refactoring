package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderLineItemDao orderLineItemDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private MenuDao menuDao;

    @DisplayName("주문 생성 시 주문항목이 없을 시 예외가 발생한다.")
    @Test
    void createWithNoOrderItem() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문 항목이 중복되거나 없는 주문 항목일 시 예외가 발생한다.")
    @Test
    void createWithInvalidOrderItem() {
        OrderLineItem 주문된_치킨세트 = createOrderLineItem(1L, 2L, 3);
        OrderLineItem 주문된_콜라세트 = createOrderLineItem(1L, 3L, 3);

        given(menuDao.countByIdIn(anyList())).willReturn(3L);

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_콜라세트), OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        OrderLineItem 주문된_치킨세트 = createOrderLineItem(1L, 2L, 3);
        OrderLineItem 주문된_콜라세트 = createOrderLineItem(1L, 3L, 3);

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.empty());

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_콜라세트), OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문테이블의 손님이 없을 경우 예외가 발생한다.")
    @Test
    void createWithEmptyTable() {
        OrderLineItem 주문된_치킨세트 = createOrderLineItem(1L, 2L, 3);
        OrderLineItem 주문된_콜라세트 = createOrderLineItem(1L, 3L, 3);
        OrderTable orderTable = createOrderTable(1L, true, null, 3);

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(orderTable));

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_콜라세트), OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderLineItem 주문된_치킨세트 = createOrderLineItem(1L, 2L, 3);
        OrderLineItem 주문된_콜라세트 = createOrderLineItem(1L, 3L, 3);
        OrderTable orderTable = createOrderTable(1L, false, null, 3);

        Order expect = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_콜라세트), OrderStatus.COOKING, 1L);

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(expect);

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_콜라세트), OrderStatus.COOKING, 1L);
        Order actual = orderService.create(order);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(1L),
            () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(1L)
        );
    }

    @DisplayName("주문 리스트를 조회한다")
    @Test
    void list() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.MEAL, null);
        Order order1 = createOrder(2L, LocalDateTime.now(), null, OrderStatus.MEAL, null);
        Order order2 = createOrder(3L, LocalDateTime.now(), null, OrderStatus.MEAL, null);
        List<Order> orders = Arrays.asList(order, order1, order2);
        given(orderDao.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Collections.EMPTY_LIST);

        List<Order> actual = orderService.list();

        assertThat(actual).isEqualTo(orders);
    }

    @DisplayName("주문 상태를 변경할 때 주문이 없을 시 예외가 발생한다.")
    @Test
    void changeOrderStatusWithInvalidOrder() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COOKING, 2L);

        given(orderDao.findById(any(Long.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 변경할 때 주문상태가 'COMPLETION' 인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWhenCompletion() {
        Order expect = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COMPLETION, 2L);

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(expect));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order expect = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COOKING, 2L);

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(expect));
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Collections.EMPTY_LIST);

        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.MEAL, 2L);
        Order actual = orderService.changeOrderStatus(1L, order);

        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
    }
}
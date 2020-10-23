package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("새로운 주문를 생성한다.")
    @Test
    void createTest() {
        // given
        Menu menu = menuDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 메뉴가 없습니다."));

        OrderTable orderTable = orderTableDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 테이블이 없습니다."));
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        // when
        Order result = orderService.create(order);

        // then
        Order savedOrder = orderDao.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        List<OrderLineItem> savedOrderLineItems = orderLineItemDao.findAllByOrderId(result.getId());

        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrderLineItems).hasSize(1);
    }

    @DisplayName("새로운 주문 생성 시, OrderLineItem이 없으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsExceptionTest() {
        Order order = new Order();
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableExceptionTest() {
        // given
        OrderTable orderTable = orderTableDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 테이블이 없습니다."));
        orderTable.setEmpty(true);
        orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 수량을 나타내는 orderLineItem는 menu를 중복으로 가지면 안된다.")
    @Test
    void changeOrderStateTest() {
        Menu menu = menuDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 메뉴가 없습니다."));

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(menu.getId());
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(menu.getId());

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 목록을 조회한다.")
    @Test
    void listTest() {
        // given
        Menu menu = menuDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 메뉴가 없습니다."));

        OrderTable orderTable = orderTableDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 테이블이 없습니다."));
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        orderService.create(order);

        // when
        List<Order> list = orderService.list();

        // when
        assertThat(list).hasSize(orderDao.findAll().size());
        assertThat(list.get(0).getOrderLineItems()).hasSize(1);
    }

    @DisplayName("Order 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        final String CHANGED_STATUS = "COMPLETION";

        // given
        Menu menu = menuDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 메뉴가 없습니다."));

        OrderTable orderTable = orderTableDao.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("저장된 테이블이 없습니다."));
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        // when
        Order result = orderService.create(order);
        result.setOrderStatus(CHANGED_STATUS);
        orderService.changeOrderStatus(result.getId(), result);

        // then
        Order changeOrder = orderDao.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        assertThat(changeOrder.getOrderStatus()).isEqualTo(CHANGED_STATUS);
    }
}
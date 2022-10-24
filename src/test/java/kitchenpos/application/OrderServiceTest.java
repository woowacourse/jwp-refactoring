package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        OrderTable saveOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 생성 시 주문 메뉴 목록이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderLineItems() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        OrderTable saveOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 시 메뉴당 하나의 주문 항목이 아니라면 예외를 반환한다.")
    void create_WhenDifferentOrderLineItemsSize() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        OrderTable saveOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setQuantity(1);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(null);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setQuantity(1);
        orderLineItem2.setMenuId(1L);
        orderLineItem2.setOrderId(null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        OrderTable saveOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        OrderTable saveOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        Order savedOrder = orderService.create(order);

        // when
        Order changeOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}

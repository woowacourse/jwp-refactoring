package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orderz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderzServiceTest {
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

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @AfterEach
    void tearDown() {
        menuProductDao.deleteAll();
        productDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
    }

    @DisplayName("새로운 주문를 생성한다.")
    @Test
    void createTest() {
        // given
        Menu menu = createMenu();
        OrderTable orderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Orderz order = new Orderz();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Orderz result = orderService.create(order);

        // then
        Orderz savedOrder = orderDao.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        List<OrderLineItem> savedOrderLineItems = orderLineItemDao.findAllByOrder(result);

        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrderLineItems).hasSize(1);
    }

    @DisplayName("새로운 주문 생성 시, OrderLineItem이 없으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsExceptionTest() {
        Orderz order = new Orderz();
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableExceptionTest() {
        // given
        OrderTable orderTable = createOrderTable(true);

        Orderz order = new Orderz();
        order.setOrderTableId(orderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 생성 시, 수량을 나타내는 orderLineItem는 menu를 중복으로 가지면 안된다.")
    @Test
    void changeOrderStateTest() {
        Menu menu = createMenu();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(menu.getId());
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(menu.getId());

        Orderz order = new Orderz();
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 목록을 조회한다.")
    @Test
    void listTest() {
        // given
        Menu menu = createMenu();
        OrderTable orderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Orderz order = new Orderz();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        orderService.create(order);

        // when
        List<Orderz> list = orderService.list();

        // when
        assertThat(list).hasSize(orderDao.findAll().size());
        assertThat(list.get(0).getOrderLineItems()).hasSize(1);
    }

    @DisplayName("Order 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        final String CHANGED_STATUS = "COMPLETION";

        // given
        Menu menu = createMenu();
        OrderTable orderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Orderz order = new Orderz();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Orderz result = orderService.create(order);
        result.setOrderStatus(CHANGED_STATUS);
        orderService.changeOrderStatus(result.getId(), result);

        // then
        Orderz changeOrder = orderDao.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("주문이 저장되지 않았습니다."));
        assertThat(changeOrder.getOrderStatus()).isEqualTo(CHANGED_STATUS);
    }

    @DisplayName("존재하지 않는 Order 상태로 변경한다.")
    @Test
    void changeOrderStatusExceptionTest() {
        final String CHANGED_STATUS = "NOT_VALID_STATUS";

        // given
        Menu menu = createMenu();

        OrderTable orderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());

        Orderz order = new Orderz();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Orderz result = orderService.create(order);
        result.setOrderStatus(CHANGED_STATUS);
        assertThatThrownBy(() -> orderService.changeOrderStatus(result.getId(), result))
                .isInstanceOf(IllegalArgumentException.class);
    }

//    private OrderTable createOrderTable() {
//        return createOrderTable(createTableGroup());
//    }
//
//    private OrderTable createOrderTable(TableGroup tableGroup) {
//        OrderTable orderTable = new OrderTable();
//        orderTable.setNumberOfGuests(10);
//        orderTable.setEmpty(true);
//        return orderTableDao.save(orderTable);
//    }
//
//    private TableGroup createTableGroup() {
//        TableGroup tableGroup = new TableGroup();
//        return tableGroupDao.save(tableGroup);
//    }
//
//    private Orderz createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
//        Orderz order = new Orderz();
//        order.setOrderTableId(orderTableId);
//        order.setOrderStatus(OrderStatus.COOKING.name());
//        order.setOrderLineItems(orderLineItems);
//        return orderDao.save(order);
//    }


    private OrderTable createOrderTable(boolean empty) {
//        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
//        orderTable.setTableGroup(tableGroup);
        orderTable.setEmpty(empty);
//        tableGroupDao.save(tableGroup);
        orderTableDao.save(orderTable);
        return orderTable;
    }

    private Menu createMenu() {
        Menu menu = new Menu();
        menu.setName("포테이토_피자");
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuGroup(menuGroupDao.save(new MenuGroup("피자")));
        return menuDao.save(menu);
    }
}
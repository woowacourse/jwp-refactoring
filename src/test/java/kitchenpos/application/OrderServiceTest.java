package kitchenpos.application;

import kitchenpos.TestDomainFactory;
import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    private TableGroup tableGroup;
    private OrderTable savedOrderTable;
    private Menu savedMenu1;
    private Menu savedMenu2;
    private Menu savedMenu3;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = TestDomainFactory.createOrderTable(0, false);
        this.tableGroup = createSavedTableGroup(LocalDateTime.now(), Collections.singletonList(orderTable));
        this.savedOrderTable = createSavedOrderTable(this.tableGroup.getId(), 0, false);

        MenuGroup savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedMenu1 = createSavedMenu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), savedMenuGroup.getId());
        this.savedMenu2 = createSavedMenu("후라이드양념두마리메뉴", BigDecimal.valueOf(27_000), savedMenuGroup.getId());
        this.savedMenu3 = createSavedMenu("후라이드간장두마리메뉴", BigDecimal.valueOf(27_000), savedMenuGroup.getId());
    }

    @DisplayName("새로운 주문 생성")
    @Test
    void createOrderTest() {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order savedOrder = this.orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(order.getOrderLineItems().size()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목이 없으면 예외 발생")
    @Test
    void createOrderWithEmptyOrderLineItemsThenThrowException() {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        order.setOrderLineItems(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목 내에서 중복되는 메뉴가 있으면 예외 발생")
    @Test
    void createOrderWithOrderLineItemsCountNotEqualToMenuCountThenThrowException() {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 존재하지 않는 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithNotExistOrderTableThenThrowException() {
        long notExistOrderTable = -1L;
        Order order = TestDomainFactory.createOrder(notExistOrderTable);
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 빈 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithEmptyOrderTableThenThrowException() {
        OrderTable savedOrderTable = createSavedOrderTable(this.tableGroup.getId(), 0, true);
        Order order = TestDomainFactory.createOrder(savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("존재하는 모든 주문을 조회")
    @Test
    void listOrderTest() {
        Order order1 = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order1.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order order2 = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem3 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 2);
        OrderLineItem orderLineItem4 = TestDomainFactory.createOrderLineItem(this.savedMenu3.getId(), 1);
        order2.setOrderLineItems(Arrays.asList(orderLineItem3, orderLineItem4));

        List<Order> orders = Arrays.asList(order1, order2);
        orders.forEach(order -> this.orderService.create(order));

        List<Order> savedOrders = this.orderService.list();

        assertThat(savedOrders.size()).isEqualTo(orders.size());
    }

    @DisplayName("특정 주문의 주문 상태를 식사 혹은 계산 완료로 변경")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusTest(String orderStatus) {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order savedOrder = this.orderService.create(order);
        order.setOrderStatus(orderStatus);

        Order changedOrder = this.orderService.changeOrderStatus(savedOrder.getId(), order);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("존재하지 않는 주문의 주문 상태를 변경하면 예외 발생")
    @Test
    void changeOrderStatusWithNotExistOrderThenThrowException() {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderStatus(OrderStatus.MEAL.name());

        Long notExistOrderId = -1L;

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(notExistOrderId, order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문의 주문 상태가 계산 완료일 때 주문 상태를 변경하면 예외 발생")
    @Test
    void changeOrderStatusWithOrderStatusIsCompletionThenThrowException() {
        Order order = TestDomainFactory.createOrder(this.savedOrderTable.getId());
        OrderLineItem orderLineItem1 = TestDomainFactory.createOrderLineItem(this.savedMenu1.getId(), 1);
        OrderLineItem orderLineItem2 = TestDomainFactory.createOrderLineItem(this.savedMenu2.getId(), 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        Order savedOrder = this.orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        this.orderDao.save(savedOrder);

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(savedOrder.getId(), order)).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createSavedTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = TestDomainFactory.createTableGroup(createdDate, orderTables);
        return this.tableGroupDao.save(tableGroup);
    }

    private OrderTable createSavedOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = TestDomainFactory.createOrderTable(numberOfGuests, empty);
        orderTable.setTableGroupId(tableGroupId);
        return this.orderTableDao.save(orderTable);
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = TestDomainFactory.createMenuGroup(menuName);
        return this.menuGroupDao.save(menuGroup);
    }

    private Menu createSavedMenu(String name, BigDecimal price, Long menuGroupId) {
        Menu menu = TestDomainFactory.createMenu(name, price, menuGroupId);
        return this.menuDao.save(menu);
    }
}
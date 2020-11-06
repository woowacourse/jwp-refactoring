package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.repository.*;
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
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private TableGroup tableGroup;
    private OrderTable savedOrderTable;
    private Menu savedMenu1;
    private Menu savedMenu2;
    private Menu savedMenu3;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderLineItem orderLineItem3;
    private OrderLineItem orderLineItem4;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(0, false);
        this.tableGroup = createSavedTableGroup(LocalDateTime.now(), Collections.singletonList(orderTable));
        this.savedOrderTable = createSavedOrderTable(this.tableGroup, 0, false);

        MenuGroup savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedMenu1 = createSavedMenu("양념간장두마리메뉴", BigDecimal.valueOf(28_000), savedMenuGroup);
        this.savedMenu2 = createSavedMenu("후라이드양념두마리메뉴", BigDecimal.valueOf(27_000), savedMenuGroup);
        this.savedMenu3 = createSavedMenu("후라이드간장두마리메뉴", BigDecimal.valueOf(27_000), savedMenuGroup);
        this.orderLineItem1 = new OrderLineItem(this.savedMenu1, 1);
        this.orderLineItem2 = new OrderLineItem(this.savedMenu2, 1);
        this.orderLineItem4 = new OrderLineItem(this.savedMenu3, 1);
        this.orderLineItem3 = new OrderLineItem(this.savedMenu2, 2);
    }

    @DisplayName("새로운 주문 생성")
    @Test
    void createOrderTest() {
        Order order = new Order(this.savedOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));

        Order savedOrder = this.orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder).isNotNull(),
                () -> assertThat(savedOrder.getOrderTable()).isEqualTo(order.getOrderTable()),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(order.getOrderLineItems().size()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목이 없으면 예외 발생")
    @Test
    void createOrderWithEmptyOrderLineItemsThenThrowException() {
        Order order = new Order(this.savedOrderTable);
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 주문 항목 내에서 중복되는 메뉴가 있으면 예외 발생")
    @Test
    void createOrderWithOrderLineItemsCountNotEqualToMenuCountThenThrowException() {
        Order order = new Order(this.savedOrderTable);
        OrderLineItem orderLineItem1 = new OrderLineItem(this.savedMenu1, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(this.savedMenu1, 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 존재하지 않는 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithNotExistOrderTableThenThrowException() {
        OrderTable notExistOrderTable = new OrderTable();
        long notExistOrderTableId = -1L;
        notExistOrderTable.setId(notExistOrderTableId);
        Order order = new Order(notExistOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문을 생성할 때 빈 테이블을 주문 테이블로 지정하면 예외 발생")
    @Test
    void createOrderWithEmptyOrderTableThenThrowException() {
        OrderTable savedOrderTable = createSavedOrderTable(this.tableGroup, 0, true);
        Order order = new Order(savedOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));

        assertThatThrownBy(() -> this.orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("존재하는 모든 주문을 조회")
    @Test
    void listOrderTest() {
        Order order1 = new Order(this.savedOrderTable);
        order1.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));
        Order order2 = new Order(this.savedOrderTable);
        order2.setOrderLineItems(Arrays.asList(this.orderLineItem3, this.orderLineItem4));

        List<Order> orders = Arrays.asList(order1, order2);
        orders.forEach(order -> this.orderService.create(order));

        List<Order> savedOrders = this.orderService.list();

        assertAll(
                () -> assertThat(savedOrders.size()).isEqualTo(orders.size()),
                () -> assertThat(savedOrders.contains(order1)),
                () -> assertThat(savedOrders.contains(order2))
        );
    }

    @DisplayName("특정 주문의 주문 상태를 식사 혹은 계산 완료로 변경")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusTest(String orderStatus) {
        Order order = new Order(this.savedOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));

        Order savedOrder = this.orderService.create(order);
        order.setOrderStatus(orderStatus);

        Order changedOrder = this.orderService.changeOrderStatus(savedOrder.getId(), order);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("존재하지 않는 주문의 주문 상태를 변경하면 예외 발생")
    @Test
    void changeOrderStatusWithNotExistOrderThenThrowException() {
        Order order = new Order(this.savedOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));
        order.setOrderStatus(OrderStatus.MEAL.name());

        Long notExistOrderId = -1L;

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(notExistOrderId, order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문의 주문 상태가 계산 완료일 때 주문 상태를 변경하면 예외 발생")
    @Test
    void changeOrderStatusWithOrderStatusIsCompletionThenThrowException() {
        Order order = new Order(this.savedOrderTable);
        order.setOrderLineItems(Arrays.asList(this.orderLineItem1, this.orderLineItem2));

        Order savedOrder = this.orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        this.orderRepository.save(savedOrder);

        assertThatThrownBy(() -> this.orderService.changeOrderStatus(savedOrder.getId(), order)).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createSavedTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        return this.tableGroupRepository.save(tableGroup);
    }

    private OrderTable createSavedOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        orderTable.setTableGroup(tableGroup);
        return this.orderTableRepository.save(orderTable);
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);
        return this.menuGroupRepository.save(menuGroup);
    }

    private Menu createSavedMenu(String name, BigDecimal price, MenuGroup menuGroup) {
        Menu menu = new Menu(name, price, menuGroup);
        return this.menuRepository.save(menu);
    }
}
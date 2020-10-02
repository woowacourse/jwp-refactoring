package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kitchenpos.DomainFactory.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        menuGroupIds = new ArrayList<>();
        menuIds = new ArrayList<>();
        orderTableIds = new ArrayList<>();
        orderIds = new ArrayList<>();
        orderLineItemSeqs = new ArrayList<>();
    }

    @DisplayName("새로운 주문 추가")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupDao, "한마리메뉴");
        Menu savedMenu = saveMenu(menuDao, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableDao, 0, false);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1L);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(orderLineItems);

        Order savedOrder = orderService.create(order);
        orderIds.add(savedOrder.getId());
        List<OrderLineItem> savedOrderLineItems = savedOrder.getOrderLineItems();
        savedOrderLineItems.forEach(item -> orderLineItemSeqs.add(item.getSeq()));

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("새로운 주문에 주문 항목이 없을 때 예외 반환")
    @Test
    void createOrderWithEmptyOrderLineItemTest() {
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문의 주문 항목에 잘못된 메뉴 선택시 예외 반환")
    @Test
    void createOrderWithInvalidOrderLineITemTest() {
        OrderLineItem orderLineItem = createOrderLineItem(0L, 1L);
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 등록 시 존재하지 않는 주문 테이블 번호 입력 시 예외 반환")
    @Test
    void createOrderWithInvalidOrderTableTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupDao, "한마리메뉴");
        Menu savedMenu = saveMenu(menuDao, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1L);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);

        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(0L);

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 등록 시 비어있는 테이블에 주문 추가 시 예외 반환")
    @Test
    void createOrderWithEmptyOrderTableTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupDao, "한마리메뉴");
        Menu savedMenu = saveMenu(menuDao, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableDao, 0, true);

        OrderLineItem orderLineItem = createOrderLineItem(savedMenu.getId(), 1L);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("저장된 모든 주문 조회")
    @Test
    void listTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupDao, "한마리메뉴");
        Menu savedMenu = saveMenu(menuDao, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableDao, 0, false);
        Order savedOrder = saveOrder(orderDao, savedOrderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        saveOrderLineItem(orderLineItemDao, savedOrder.getId(), savedMenu.getId(), 1L);

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문의 상태 변경")
    @Test
    void changeOrderStatusTest() {
        OrderTable savedOrderTable = saveOrderTable(orderTableDao, 0, false);
        Order savedOrder = saveOrder(orderDao, savedOrderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());

        Order newStatusOrder = new Order();
        newStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        Order changeStatusOrder = orderService.changeOrderStatus(savedOrder.getId(), newStatusOrder);

        assertAll(
                () -> assertThat(changeStatusOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(changeStatusOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
                () -> assertThat(changeStatusOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime()),
                () -> assertThat(changeStatusOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name())
        );
    }

    @DisplayName("주문 상태 변경 시 존재하지 않는 주문 입력 시 예외 반환")
    @Test
    void changeOrderStatusWithInvalidOrderTest() {
        Order order = new Order();

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(0L, order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 상태 변경 시 예외 반환")
    @Test
    void changeOrderStatusWithCompletionOrderTest() {
        OrderTable savedOrderTable = saveOrderTable(orderTableDao, 0, false);
        Order savedOrder = saveOrder(orderDao, savedOrderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now());

        Order newStatusOrder = new Order();

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(savedOrder.getId(), newStatusOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        deleteOrderLineItem();
        deleteOrder();
        deleteOrderTable();
        deleteMenu();
        deleteMenuGroup();
    }
}
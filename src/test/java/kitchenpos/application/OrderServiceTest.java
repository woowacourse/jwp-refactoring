package kitchenpos.application;

import static kitchenpos.db.TestDataFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("주문 생성 실패 - 주문 항목 없음")
    @Test
    void createFail_When_OrderLineItems_Not_Exist() {
        Order order = createOrder(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), null);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 항목 중복")
    @Test
    void createFail_When_Invalid_OrderLineItems() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(), 2);

        Order order = createOrder(null, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            Arrays.asList(orderLineItem, orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 존재하지 않는 주문 테이블")
    @Test
    void createFail_When_Invalid_OrderTable() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(), 2);
        OrderTable orderTable = createOrderTable(null, null, 3, false);
        orderTableDao.save(orderTable);

        Order order = createOrder(null, -1L, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 비어있는 테이블")
    @Test
    void createFail_When_OrderTable_IsEmpty() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(), 2);
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            Arrays.asList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(), 2);
        OrderTable savedOrderTable = saveOrderTable();

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            Arrays.asList(orderLineItem));

        Order savedOrder = orderService.create(order);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문 리스트 조회")
    @Test
    void list() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(), 2);
        OrderTable savedOrderTable = saveOrderTable();

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            Arrays.asList(orderLineItem));

        Order savedOrder = orderService.create(order);

        List<Order> orders = orderService.list();

        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
            () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(savedOrder.getOrderStatus()),
            () -> assertThat(orders.get(0).getId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 변경 실패 - 존재하지 않는 주문")
    @Test
    void changeOrderStatusFail_When_Order_Not_Exist() {
        Order order = createOrder(null, 1L, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 변경")
    @Test
    void changeOrderStatus() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        Menu menu = saveMenu(menuGroup, product);

        OrderLineItem orderLineItem = createOrderLineItem(null, menu.getId(),2);
        OrderTable savedOrderTable = saveOrderTable();
        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order savedOrder = orderService.create(order);

        Order order2 = createOrder(null, 1L, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList(orderLineItem));

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), order2);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

    }

    private Product saveProduct() {
        Product product = createProduct(null, "치킨", BigDecimal.valueOf(15_000L));
        return productDao.save(product);
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = createMenuGroup(null, "치킨류");
        return menuGroupDao.save(menuGroup);
    }

    private Menu saveMenu(MenuGroup menuGroup, Product product) {
        MenuProduct menuProduct = createMenuProduct(null, product.getId(), 1L);
        Menu menu = createMenu(null, "우형치킨", BigDecimal.valueOf(14_000L), menuGroup.getId(),
            Arrays.asList(menuProduct));
        return menuDao.save(menu);
    }

    private OrderTable saveOrderTable() {
        OrderTable orderTable = createOrderTable(null, null, 3, false);
        return orderTableDao.save(orderTable);
    }
}

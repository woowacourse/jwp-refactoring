package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderLineItemDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@DisplayName("Order 서비스 테스트")
class OrderServiceTest {

    private OrderService orderService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private Menu savedMenu;
    private OrderTable savedOrderTable;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();
        final MenuDao menuDao = new FakeMenuDao();
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();

        orderService = new OrderService(menuDao, orderDao, new FakeOrderLineItemDao(), orderTableDao);

        // menu group
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");
        savedMenuGroup = menuGroupDao.save(menuGroup);

        // product
        final Product product = new Product();
        product.setName("상품");
        product.setPrice(new BigDecimal(1000));
        savedProduct = productDao.save(product);

        // menu
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(savedMenuGroup.getId());

        // menu product
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct));

        savedMenu = menuDao.save(menu);

        // order table
        final OrderTable orderTable = new OrderTable();
        savedOrderTable = orderTableDao.save(orderTable);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        final Order order = new Order();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(savedOrderTable.getId());

        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 등록 시 주문 항목이 비어있으면 안된다")
    @Test
    void createOrderLineItemIsEmpty() {
        final Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 항목이 메뉴에 존재해야 한다")
    @Test
    void createOrderLineItemIsNotExist() {
        final Order order = new Order();
        final OrderLineItem notSavedOrderLineItem = new OrderLineItem();
        notSavedOrderLineItem.setMenuId(savedMenu.getId());
        order.setOrderLineItems(List.of(notSavedOrderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final Order order = new Order();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        long notSavedOrderTableId = 0L;
        order.setOrderTableId(notSavedOrderTableId);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsEmpty() {
        final Order order = new Order();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable savedEmptyOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedEmptyOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회한다")
    @Test
    void list() {
        final List<Order> orders = orderService.list();

        assertThat(orders).hasSize(0);
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        final Order savedOrder = orderDao.save(order);

        final Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문의 상태 변경 시 주문이 존재해야 한다")
    @Test
    void changeOrderStatusOrderIsNotExist() {
        final long notSavedOrderId = 0L;

        assertThatThrownBy(() -> orderService.changeOrderStatus(notSavedOrderId, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태 변경 시 주문이 완료된 상태면 안된다")
    @Test
    void changeOrderStatusOrderIsCompletion() {
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        final Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

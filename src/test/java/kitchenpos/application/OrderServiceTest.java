package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuGroup;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createProduct;
import static kitchenpos.TestObjectFactory.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao tableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        savedMenuGroup = menuGroupDao.save(createMenuGroup());
        savedProduct = productDao.save(createProduct(18_000));
        menuProduct = createMenuProduct(savedProduct);
        menuProducts = Arrays.asList(menuProduct);
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = TestObjectFactory.createOrder(savedTable, orderLineItems);
        Order create = orderService.create(order);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(create.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @DisplayName("[예외] 주문 항목이 없는 주문 추가")
    @Test
    void create_Fail_With_NoOrderLineItem() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Order order = createOrder(savedTable, null);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴가 포함된 주문 추가")
    @Test
    void create_Fail_With_NotExistMenu() {
        OrderTable table = tableDao.save(createTable(false));

        Menu notSavedMenu = createMenu(18_000, savedMenuGroup, menuProducts);
        OrderLineItem orderLineItem = createOrderLineItem(notSavedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(table, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable notSavedTable = createTable(false);

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(notSavedTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 주문 추가")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable emptyTable = tableDao.save(createTable(true));

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(emptyTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(savedTable, orderLineItems);

        orderService.create(order);
        orderService.create(order);

        List<Order> list = orderService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(savedTable, orderLineItems);

        Order create = orderService.create(order);
        Order target = Order.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();

        Order changeOrderStatus = orderService.changeOrderStatus(create.getId(), target);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(target.getOrderStatus());
    }

    @DisplayName("[예외] 이미 완료된 주문의 상태 변경")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = menuService.create(createMenu(18_000, savedMenuGroup, menuProducts));
        OrderLineItem orderLineItem = createOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createOrder(savedTable, orderLineItems);

        Order create = orderService.create(order);
        Order target = Order.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();
        orderService.changeOrderStatus(create.getId(), target);

        assertThatThrownBy(() -> orderService.changeOrderStatus(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
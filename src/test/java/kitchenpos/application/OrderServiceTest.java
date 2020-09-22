package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @BeforeEach
    void setUp() {
        savedMenuGroup = menuGroupDao.save(createTestMenuGroup());
        savedProduct = productDao.save(createTestProduct());
        menuProduct = createTestMenuProduct(savedProduct);
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        OrderTable savedTable = tableDao.save(createTestTable(false));

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(savedTable, orderLineItems);
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
        OrderTable savedTable = tableDao.save(createTestTable(false));

        Order order = createTestOrder(savedTable, null);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴가 포함된 주문 추가")
    @Test
    void create_Fail_With_NotExistMenu() {
        OrderTable table = tableDao.save(createTestTable(false));

        Menu notSavedMenu = createTestMenu(savedMenuGroup, menuProduct);
        OrderLineItem orderLineItem = createTestOrderLineItem(notSavedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(table, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable notSavedTable = createTestTable(false);

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(notSavedTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 주문 추가")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable emptyTable = tableDao.save(createTestTable(true));

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(emptyTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        OrderTable savedTable = tableDao.save(createTestTable(false));

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(savedTable, orderLineItems);

        orderService.create(order);
        orderService.create(order);

        List<Order> list = orderService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable savedTable = tableDao.save(createTestTable(false));

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(savedTable, orderLineItems);

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
        OrderTable savedTable = tableDao.save(createTestTable(false));

        Menu savedMenu = menuService.create(createTestMenu(savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = createTestOrderLineItem(savedMenu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        Order order = createTestOrder(savedTable, orderLineItems);

        Order create = orderService.create(order);
        Order target = Order.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();
        orderService.changeOrderStatus(create.getId(), target);

        assertThatThrownBy(() -> orderService.changeOrderStatus(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createTestOrder(OrderTable table, List<OrderLineItem> orderLineItems) {
        return Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(orderLineItems)
            .orderedTime(LocalDateTime.now())
            .build();
    }

    private OrderTable createTestTable(boolean empty) {
        return OrderTable.builder()
            .empty(empty)
            .build();
    }

    private OrderLineItem createTestOrderLineItem(Menu menu) {
        return OrderLineItem.builder()
            .menuId(menu.getId())
            .quantity(2)
            .build();
    }

    private Menu createTestMenu(MenuGroup menuGroup, MenuProduct menuProduct) {
        return Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();
    }

    private MenuGroup createTestMenuGroup() {
        return MenuGroup.builder()
            .name("강정메뉴")
            .build();
    }

    private MenuProduct createTestMenuProduct(Product product) {
        return MenuProduct.builder()
            .productId(product.getId())
            .quantity(1)
            .build();
    }

    private Product createTestProduct() {
        return Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .build();
    }
}
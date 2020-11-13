package kitchenpos.order.application;

import static kitchenpos.TestObjectFactory.createMenuGroup;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.repository.MenuGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.repository.OrderTableDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup savedMenuGroup;

    @BeforeEach
    void setUp() {
        savedMenuGroup = menuGroupDao.save(createMenuGroup("강정메뉴"));
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(savedTable, orderLineItems);
        OrderResponse savedOrder = orderService.create(request);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @DisplayName("[예외] 주문 항목이 없는 주문 추가")
    @Test
    void create_Fail_With_NoOrderLineItem() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(false));

        OrderRequest request = createOrderRequest(savedTable, null);

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴가 포함된 주문 추가")
    @Test
    void create_Fail_With_NotExistMenu() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(false));

        Product product = createProduct(10_000);
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
        Menu notSavedMenu = Menu.builder()
            .price(BigDecimal.valueOf(18_000))
            .menuProducts(menuProducts)
            .build();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(notSavedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(savedTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = OrderRequest.builder()
            .orderTableId(10000L)
            .orderLineItems(orderLineItems)
            .build();

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(savedTable, orderLineItems);

        orderService.create(request);
        orderService.create(request);

        List<OrderResponse> list = orderService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest order = createOrderRequest(savedTable, orderLineItems);

        OrderResponse savedOrder = orderService.create(order);
        OrderStatusChangeRequest request = OrderStatusChangeRequest.builder()
            .orderStatus(OrderStatus.COMPLETION)
            .build();

        OrderResponse changeOrderStatus = orderService
            .changeOrderStatus(savedOrder.getId(), request);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    private Menu saveMenu() {
        Product product = productDao.save(createProduct(10_000));
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroup(savedMenuGroup)
            .menuProducts(menuProducts)
            .build();
        return menuDao.save(menu);
    }

    private OrderLineItemRequest createOrderLineItemRequest(Menu savedMenu) {
        return OrderLineItemRequest.builder()
            .menuId(savedMenu.getId())
            .quantity(2)
            .build();
    }

    private OrderRequest createOrderRequest(OrderTable savedTable,
        List<OrderLineItemRequest> orderLineItems) {
        return OrderRequest.builder()
            .orderTableId(savedTable.getId())
            .orderLineItems(orderLineItems)
            .build();
    }
}
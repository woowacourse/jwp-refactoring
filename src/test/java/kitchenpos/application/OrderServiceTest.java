package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createMenuGroup;
import static kitchenpos.TestObjectFactory.createMenuProductRequest;
import static kitchenpos.TestObjectFactory.createMenuRequest;
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
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
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

    @BeforeEach
    void setUp() {
        savedMenuGroup = menuGroupDao.save(createMenuGroup("강정메뉴"));
        savedProduct = productDao.save(createProduct(18_000));
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(savedTable, orderLineItems);
        Order savedOrder = orderService.create(request);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @DisplayName("[예외] 주문 항목이 없는 주문 추가")
    @Test
    void create_Fail_With_NoOrderLineItem() {
        OrderTable savedTable = tableDao.save(createTable(false));

        OrderRequest request = createOrderRequest(savedTable, null);

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴가 포함된 주문 추가")
    @Test
    void create_Fail_With_NotExistMenu() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu notSavedMenu = Menu.builder()
            .id(1000L)
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
        OrderTable notSavedTable = OrderTable.builder()
            .id(1000L)
            .build();

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(notSavedTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 주문 추가")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable emptyTable = tableDao.save(createTable(true));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(emptyTable, orderLineItems);

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest request = createOrderRequest(savedTable, orderLineItems);

        orderService.create(request);
        orderService.create(request);

        List<Order> list = orderService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest order = createOrderRequest(savedTable, orderLineItems);

        Order savedOrder = orderService.create(order);
        OrderStatusChangeRequest request = OrderStatusChangeRequest.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();

        Order changeOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), request);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    @DisplayName("[예외] 이미 완료된 주문의 상태 변경")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        OrderTable savedTable = tableDao.save(createTable(false));

        Menu savedMenu = saveMenu();

        OrderLineItemRequest orderLineItem = createOrderLineItemRequest(savedMenu);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(orderLineItem);

        OrderRequest order = createOrderRequest(savedTable, orderLineItems);

        Order savedOrder = orderService.create(order);
        OrderStatusChangeRequest request = OrderStatusChangeRequest.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();
        orderService.changeOrderStatus(savedOrder.getId(), request);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu saveMenu() {
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
        return menuService.create(createMenuRequest(18_000, savedMenuGroup, menuProducts));
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
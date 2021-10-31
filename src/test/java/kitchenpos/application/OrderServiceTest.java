package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.application.dto.TableRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class OrderServiceTest {
    private static final TableRequest TABLE_REQUEST_THREE_NON_EMPTY = new TableRequest(3, false);

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;
    @Autowired
    private MenuRepository menuRepository;

    private OrderTable table;
    private Product chicken;
    private Product pizza;
    private Product frenchFry;

    private MenuGroup menuGroup;

    private Menu chickenSet;
    private Menu pizzaSet;

    @BeforeEach
    void setUp() {
        table = tableService.create(TABLE_REQUEST_THREE_NON_EMPTY);

        menuGroup = menuGroupService.create(new MenuGroup("menuGroup"));

        chicken = productService.create(new Product("chicken", BigDecimal.valueOf(20000)));
        pizza = productService.create(new Product("pizza", BigDecimal.valueOf(15000)));
        frenchFry = productService.create(new Product("frenchFry", BigDecimal.valueOf(2000)));

        chickenSet = menuService.create(new MenuRequest("chickenSet", BigDecimal.valueOf(21000), menuGroup.getId(),
                Arrays.asList(new MenuProductRequest(this.chicken.getId(), 1L),
                        new MenuProductRequest(this.frenchFry.getId(), 1L))));
        pizzaSet = menuService.create(new MenuRequest("pizzaSet", BigDecimal.valueOf(16000), menuGroup.getId(),
                Arrays.asList(new MenuProductRequest(this.pizza.getId(), 1L),
                        new MenuProductRequest(this.frenchFry.getId(), 1L))));
    }

    @Test
    @DisplayName("주문 정상 등록")
    void create() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        OrderRequest order = new OrderRequest(table.getId(), orderLineItems);

        Order saved = orderService.create(order);
        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.findAllByOrderId(saved.getId());

        assertNotNull(saved.getId());
        assertNotNull(saved.getOrderedTime());
        assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(saved.getOrderLineItems().toList())
                .hasSize(orderLineItems.size())
                .allMatch(orderLineItem -> Objects.equals(orderLineItem.getOrder().getId(), saved.getId()))
                .allMatch(orderLineItem -> Objects.nonNull(orderLineItem.getSeq()));
        assertThat(savedOrderLineItems).hasSize(orderLineItems.size());
    }

    @Test
    @DisplayName("주문 등록 실패 :: 빈 주문 항목")
    void createWithEmptyOrderLine() {
        OrderRequest order = new OrderRequest(table.getId(), Collections.EMPTY_LIST);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 존재하지 않는 메뉴 포함")
    void createWithNotExistingMenu() {
        Long notExistingMenuId = Long.MAX_VALUE;
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(notExistingMenuId, 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        OrderRequest order = new OrderRequest(table.getId(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 같은 메뉴의 중복된 주문 항목 포함")
    void createWithDuplicateOrderLineItem() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(pizzaSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        OrderRequest order = new OrderRequest(table.getId(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 존재하지 않는 주문 테이블")
    void createWithNotExistingOrderTable() {
        Long notExistingTableId = Long.MAX_VALUE;
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        OrderRequest order = new OrderRequest(notExistingTableId, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 비어야 하는 주문 테이블로의 주문")
    void createWithEmptyStateOrderTable() {
        tableService.changeEmpty(table.getId(), new TableRequest(null, true));

        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        OrderRequest order = new OrderRequest(table.getId(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 정상 조회")
    void list() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        orderService.create(new OrderRequest(table.getId(), orderLineItems));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
        assertThat(orders).allMatch(order -> !order.getOrderLineItems().toList().isEmpty());
    }

    @ParameterizedTest(name = "주문 상태 정상 수정 :: -> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        Long notExistingOrderId = Long.MAX_VALUE;

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(notExistingOrderId, new OrderStatusRequest(orderStatus)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 상태 수정 실패 :: 존재하지 않는 주문-> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusNotExistingOrder(String orderStatus) {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        Order order = orderService.create(new OrderRequest(table.getId(), orderLineItems));

        Order saved = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(orderStatus));

        assertThat(saved.getOrderStatus().name()).isEqualTo(orderStatus);
    }

    @ParameterizedTest(name = "주문 상태 수정 실패 :: 이미 완료된 주문 주문-> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusAlreadyCompeted(String orderStatus) {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(chickenSet.getId(), 2L),
                new OrderLineItemRequest(pizzaSet.getId(), 1L));
        Order order = orderService.create(new OrderRequest(table.getId(), orderLineItems));
        orderService.changeOrderStatus(order.getId(), new OrderStatusRequest("COMPLETION"));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(orderStatus)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        menuProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
    }
}

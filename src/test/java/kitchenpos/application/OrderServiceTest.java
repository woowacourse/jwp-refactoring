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
        table = tableService.create(new OrderTable(false));

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
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = new Order(table.getId(), null, null, orderLineItems);

        Order saved = orderService.create(order);
        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.findAllByOrderId(saved.getId());

        assertNotNull(saved.getId());
        assertNotNull(saved.getOrderedTime());
        assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(saved.getOrderLineItems())
                .hasSize(orderLineItems.size())
                .allMatch(orderLineItem -> Objects.equals(orderLineItem.getOrderId(), saved.getId()));
        assertThat(savedOrderLineItems).hasSize(orderLineItems.size());
    }

    @Test
    @DisplayName("주문 등록 실패 :: 빈 주문 항목")
    void createWithEmptyOrderLine() {
        Order order = new Order(table.getId(), null, null, Collections.EMPTY_LIST);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 존재하지 않는 메뉴 포함")
    void createWithNotExistingMenu() {
        Long notExistingMenuId = Long.MAX_VALUE;
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(notExistingMenuId, 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = new Order(table.getId(), null, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 같은 메뉴의 중복된 주문 항목 포함")
    void createWithDuplicateOrderLineItem() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(pizzaSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = new Order(table.getId(), null, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 존재하지 않는 주문 테이블")
    void createWithNotExistingOrderTable() {
        Long notExistingTableId = Long.MAX_VALUE;
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = new Order(notExistingTableId, null, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 실패 :: 비어야 하는 주문 테이블로의 주문")
    void createWithEmptyStateOrderTable() {
        tableService.changeEmpty(table.getId(), new OrderTable(true));

        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = new Order(table.getId(), null, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 정상 조회")
    void list() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        orderService.create(new Order(table.getId(), null, null, orderLineItems));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
        assertThat(orders).allMatch(order -> !order.getOrderLineItems().isEmpty());
    }

    @ParameterizedTest(name = "주문 상태 정상 수정 :: -> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        Long notExistingOrderId = Long.MAX_VALUE;

        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistingOrderId, new Order(orderStatus)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 상태 수정 실패 :: 존재하지 않는 주문-> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusNotExistingOrder(String orderStatus) {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = orderService.create(new Order(table.getId(), null, null, orderLineItems));

        Order saved = orderService.changeOrderStatus(order.getId(), new Order(orderStatus));

        assertThat(saved.getOrderStatus()).isEqualTo(orderStatus);
    }

    @ParameterizedTest(name = "주문 상태 수정 실패 :: 이미 완료된 주문 주문-> {0}")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusAlreadyCompeted(String orderStatus) {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(chickenSet.getId(), 2),
                new OrderLineItem(pizzaSet.getId(), 1));
        Order order = orderService.create(new Order(table.getId(), null, null, orderLineItems));
        orderService.changeOrderStatus(order.getId(), new Order("COMPLETION"));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderStatus)))
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

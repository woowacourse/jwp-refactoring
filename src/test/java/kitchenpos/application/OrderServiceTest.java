package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderService orderService;

    private Long menuId;
    private Long orderTableId;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("NAME"));
        Product product = productRepository.save(new Product("NAME", BigDecimal.ONE));
        MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(1L, product, 1L));
        Menu menu = menuRepository.save(new Menu("NAME", BigDecimal.ONE, menuGroup, Collections.singletonList(menuProduct)));
        menuId = menu.getId();

        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        orderTableId = orderTable.getId();
    }

    @DisplayName("주문 생성")
    @Nested
    class createOrder {

        @DisplayName("주문을 생성한다.")
        @Test
        void create() {
            OrderRequest orderRequest = createOrderRequest(orderTableId, menuId);
            OrderResponse savedOrder = orderService.create(orderRequest);
            SoftAssertions.assertSoftly(it -> {
                        it.assertThat(savedOrder).isNotNull();
                        it.assertThat(savedOrder.getId()).isNotNull();
                        it.assertThat(savedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
                    }
            );
        }

        @DisplayName("주문 항목이 1개 이상이어야한다.")
        @Test
        void createWithInvalidOrderItemList() {
            OrderRequest orderRequest = createOrderRequest(orderTableId, Collections.emptyList());
            assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목의 메뉴가 존재해야 한다.")
        @Test
        void createWithNonexistentMenu() {
            menuId = Long.MAX_VALUE;
            OrderRequest orderRequest = createOrderRequest(orderTableId, menuId);
            assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블은 비어있지 않아야한다.")
        @Test
        void createWithEmptyTable() {
            OrderTable emptyTable = orderTableRepository.save(new OrderTable(1, true));
            OrderRequest orderRequest = createOrderRequest(orderTableId, emptyTable.getId());
            assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 반환한다.")
    @Test
    void list() {
        OrderRequest orderRequest = createOrderRequest(orderTableId, menuId);
        List<OrderResponse> expected = Collections.singletonList(orderService.create(orderRequest));
        List<OrderResponse> result = orderService.list();
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        private Long savedOrderId;

        @BeforeEach
        void setUp() {
            OrderRequest orderRequest = createOrderRequest(orderTableId, menuId);
            savedOrderId = orderService.create(orderRequest).getId();
        }

        @DisplayName("주문의 상태를 변경한다.")
        @Test
        void changeOrderStatus() {
            OrderStatus newStatus = OrderStatus.MEAL;
            OrderRequest orderRequest = createOrderRequest(orderTableId, newStatus, menuId);
            OrderResponse orderResponse = orderService.changeOrderStatus(savedOrderId, orderRequest);
            assertAll(
                    () -> assertThat(orderResponse).isNotNull(),
                    () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(newStatus)
            );
        }

        @DisplayName("주문 완료 상태의 주문은 상태를 변경할 수 없다.")
        @Test
        void changeOrderStatusInCompletion() {
            OrderRequest createRequest = createOrderRequest(orderTableId, OrderStatus.COOKING, menuId);
            savedOrderId = orderService.create(createRequest).getId();
            orderService.changeOrderStatus(savedOrderId, createOrderRequest(orderTableId, OrderStatus.COMPLETION, menuId));

            OrderRequest updateRequest = createOrderRequest(orderTableId, OrderStatus.MEAL, menuId);
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, updateRequest)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @AfterEach
    void tearDown() {
        List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            menu.setMenuProducts(null);
        }
        menuRepository.saveAll(menus);

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setOrderLineItems(null);
        }
        orderRepository.saveAll(orders);

        orderLineItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}

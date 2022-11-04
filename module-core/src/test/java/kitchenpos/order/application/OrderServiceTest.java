package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.order.application.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.order.application.dto.request.order.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.order.OrderRequest;
import kitchenpos.order.domain.MenuHistory;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.MenuHistoryRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.support.Fixture;
import kitchenpos.support.application.ServiceTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class OrderServiceTest {

    private final OrderService orderService;
    private final MenuRepository menuRepository;
    private final MenuHistoryRepository menuHistoryRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceTest(final OrderService orderService,
                            final MenuRepository menuRepository,
                            final MenuHistoryRepository menuHistoryRepository,
                            final OrderTableRepository orderTableRepository,
                            final OrderRepository orderRepository,
                            final ProductRepository productRepository
    ) {
        this.orderService = orderService;
        this.menuRepository = menuRepository;
        this.menuHistoryRepository = menuHistoryRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @ServiceTest
    class CreateTest {

        private Product product;
        private Menu menu;
        private MenuHistory menuHistory;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            this.product = productRepository.save(Fixture.makeProduct("비빔밥", 5000L));
            this.menu = menuRepository.save(Fixture.makeMenu("자장면", 5000, 1L,
                    List.of(new MenuProduct(product.getId(), 10))));
            this.menuHistory = menuHistoryRepository.save(MenuHistory.copyOf(menu));
            this.orderTable = orderTableRepository.save(Fixture.makeNonEmptyOrderTable(10));
        }

        @DisplayName("주문을 한다")
        @Test
        void create() {
            final var orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = Fixture.makeOrderRequest(orderTable.getId(), orderLineItemRequest);

            final var actual = orderService.create(request);
            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("하나 이상의 주문 항목을 포함해야 한다")
        @Test
        void createWithEmptyOrderLineItems() {
            final var request = Fixture.makeOrderRequest(1L);

            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 비어 있습니다.");
        }

        @DisplayName("주문 항목의 메뉴는 서로 달라야 한다")
        @Test
        void createWithDuplicatedMenu() {
            final var request = new OrderRequest(1L, List.of(
                    new OrderLineItemRequest(menu.getId(), 1),
                    new OrderLineItemRequest(menu.getId(), 10)));

            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복된 메뉴의 주문 항목이 존재합니다.");
        }

        @DisplayName("주문하고자 하는 주문 테이블은 공석이 아니어야 한다")
        @Test
        void createWithEmptyOrderTable() {
            final var emptyOrderTable = orderTableRepository.save(Fixture.makeEmptyOrderTable());

            final var OrderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = Fixture.makeOrderRequest(emptyOrderTable.getId(), OrderLineItemRequest);

            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어 있습니다.");
        }

        @DisplayName("존재하는 주문 테이블이어야 주문을 할 수 있다")
        @Test
        void createWithNonExistOrderTable() {
            final var nonExistOrderTableId = 0L;

            final var OrderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = Fixture.makeOrderRequest(nonExistOrderTableId, OrderLineItemRequest);

            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }
    }

    @Nested
    @ServiceTest
    class ChangeOrderStatusTest {

        @DisplayName("주문 상태를 변경한다")
        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        void changeOrderStatus(final String expectedOrderStatus) {
            final var savedOrder = saveOrder();

            final var request = new ChangeOrderStatusRequest(expectedOrderStatus);
            final var actual = orderService.changeOrderStatus(savedOrder.getId(), request);

            assertThat(actual.getOrderStatus()).isEqualTo(expectedOrderStatus);
        }

        @DisplayName("결제가 완료되지 않은 주문이어야 한다")
        @Test
        void changeOrderStatusWithCompletedOrder() {
            final var savedOrder = saveCompletedOrder();
            final var request = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            final var orderId = savedOrder.getId();
            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 결제 완료된 주문입니다.");
        }

        private org.junit.jupiter.api.Order saveCompletedOrder() {
            final var order = new org.junit.jupiter.api.Order(1L, Fixture.makeSingleOrderLineItems());
            order.updateOrderStatus(OrderStatus.COMPLETION);
            return orderRepository.save(order);
        }

        @DisplayName("존재하는 주문이어야 한다")
        @Test
        void changeOrderStatusWithNonExistOrder() {
            final var nonExistOrderId = 0L;

            final var request = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(nonExistOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 존재하지 않습니다.");
        }
    }

    @DisplayName("주문 테이블을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        saveOrderAsTimes(expectedSize);

        final var actual = orderService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveOrderAsTimes(final int times) {
        for (int i = 0; i < times; i++) {
            final var order = new org.junit.jupiter.api.Order(1L, Fixture.makeSingleOrderLineItems());
            orderRepository.save(order);
        }
    }

    private org.junit.jupiter.api.Order saveOrder() {
        final var order = new Order(1L, Fixture.makeSingleOrderLineItems());
        return orderRepository.save(order);
    }
}

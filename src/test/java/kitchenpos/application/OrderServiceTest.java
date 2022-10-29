package kitchenpos.application;

import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderLineItemFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.fixtures.domain.OrderFixture.OrderRequestBuilder;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTableResponse savedOrderTable;
    private MenuGroupResponse savedMenuGroup;
    private ProductResponse savedProduct;
    private MenuResponse savedMenu;

    private OrderLineItem createdOrderLineItem;

    @BeforeEach
    void setUp() {
        savedOrderTable = saveOrderTable(10, false);
        savedMenuGroup = saveMenuGroup("메뉴 그룹");
        savedProduct = saveProduct("상품", 5_000);
        savedMenu = saveMenu("메뉴", 10_000, savedMenuGroup.toEntity(), List.of(
                createMenuProduct(savedProduct.getId(), 10)
        ));
        createdOrderLineItem = createOrderLineItem(savedMenu.getId(), 10);

    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            OrderRequest request = new OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when
            OrderResponse actual = orderService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            });
        }

        @DisplayName("주문의 주문 항목이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given
            OrderRequest request = new OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 항목의 메뉴가 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuDoesNotExist() {
            // given
            OrderLineItem orderLineItemHasNotSavedMenu = createOrderLineItem(savedMenu.getId() + 1, 1L);

            OrderRequest request = new OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId())
                    .addOrderLineItem(orderLineItemHasNotSavedMenu)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderRequest request = new OrderRequestBuilder()
                    .orderTableId(savedOrderTable.getId() + 1)
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTableResponse emptyOrderTable = saveOrderTable(10, true);

            OrderRequest request = new OrderRequestBuilder()
                    .orderTableId(emptyOrderTable.getId())
                    .addOrderLineItem(createdOrderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 모든 주문 목록을 조회한다.")
        @Test
        void Should_ReturnAllOrderList() {
            // given
            int expected = 3;
            for (int i = 0; i < expected; i++) {
                Order order = createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(createdOrderLineItem));
                orderRepository.save(order);
            }

            // when
            List<OrderResponse> actual = orderService.list();

            // then
            assertThat(actual).hasSize(expected);
        }
    }

    @DisplayName("changeOrderStatus 메소드는")
    @Nested
    class ChangeOrderStatusMethod {

        @DisplayName("주문의 주문 상태를 변경한다.")
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        @ParameterizedTest
        void Should_ChangeOrderStatus(final String after) {
            // given
            Order oldOrder = orderRepository.save(
                    createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                            List.of(createdOrderLineItem)));

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(after);

            // when
            OrderResponse actual = orderService.changeOrderStatus(oldOrder.getId(), request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(after);
        }

        @DisplayName("주문이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderDoesNotExist() {
            // given
            Order order = orderRepository.save(
                    createOrder(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                            List.of(createdOrderLineItem)));

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 상태가 COMPLETION 이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            Order completionOrder = orderRepository.save(
                    createOrder(savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
                            List.of(createdOrderLineItem)));

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

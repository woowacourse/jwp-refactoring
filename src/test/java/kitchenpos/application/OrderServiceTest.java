package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTableResponse savedOrderTable;
    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private MenuResponse savedMenu;

    private OrderLineItem createdOrderLineItem;

    @BeforeEach
    void setUp() {
        savedOrderTable = saveOrderTable(10, false);
        savedMenuGroup = saveMenuGroup("메뉴 그룹").toEntity();
        savedProduct = saveProduct("상품", 5_000).toEntity();
        savedMenu = saveMenu("메뉴", 10_000, savedMenuGroup, List.of(
                new MenuProduct(savedProduct, 10)
        ));
        createdOrderLineItem = new OrderLineItem(savedMenu.getId(), 10);

    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            OrderRequest request = new OrderRequest(savedOrderTable.getId(),
                    List.of(new OrderLineItemRequest(createdOrderLineItem)));

            // when
            OrderResponse actual = orderService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            });
        }

        @DisplayName("주문의 주문 항목이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given
            OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 항목의 메뉴가 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuDoesNotExist() {
            // given
            OrderLineItem orderLineItemHasNotSavedMenu = new OrderLineItem(savedMenu.getId() + 1, 1L);

            OrderRequest request = new OrderRequest(savedOrderTable.getId(),
                    List.of(new OrderLineItemRequest(orderLineItemHasNotSavedMenu)));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderRequest request = new OrderRequest(savedOrderTable.getId() + 1,
                    List.of(new OrderLineItemRequest(createdOrderLineItem)));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTableResponse emptyOrderTable = saveOrderTable(10, true);

            OrderRequest request = new OrderRequest(emptyOrderTable.getId(),
                    List.of(new OrderLineItemRequest(createdOrderLineItem)));

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
                OrderRequest request = new OrderRequest(savedOrderTable.getId(),
                        List.of(new OrderLineItemRequest(createdOrderLineItem)));
                orderService.create(request);
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
        void Should_ChangeOrderStatus(final OrderStatus after) {
            // given
            OrderRequest createRequest = new OrderRequest(savedOrderTable.getId(),
                    List.of(new OrderLineItemRequest(createdOrderLineItem)));
            OrderResponse oldOrder = orderService.create(createRequest);

            OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(after);

            // when
            OrderResponse actual = orderService.changeOrderStatus(oldOrder.getId(), updateRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(after);
        }

        @DisplayName("주문이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderDoesNotExist() {
            // given
            OrderRequest createRequest = new OrderRequest(savedOrderTable.getId(),
                    List.of(new OrderLineItemRequest(createdOrderLineItem)));
            OrderResponse order = orderService.create(createRequest);

            OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, updateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 상태가 COMPLETION 이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(10, false));
            Order completionOrder = new Order(orderTable, List.of(createdOrderLineItem));
            completionOrder.changeOrderStatus(OrderStatus.COMPLETION);
            Order savedOrder = orderRepository.save(completionOrder);

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

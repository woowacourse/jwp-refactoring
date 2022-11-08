package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.support.ServiceTest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    private OrderTable savedOrderTable;
    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private Menu savedMenu;

    private OrderLineItem createdOrderLineItem;

    @BeforeEach
    void setUp() {
        savedOrderTable = saveOrderTable(10, false);
        savedMenuGroup = saveMenuGroup("메뉴 그룹");
        savedProduct = saveProduct("상품", 5_000);
        savedMenu = saveMenu("메뉴", 10_000, savedMenuGroup, List.of(
                new MenuProduct(savedProduct.getId(), 10)
        ));
        createdOrderLineItem = new OrderLineItem(
                new OrderedMenu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice().getValue()), 10);
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    createdOrderLineItem.getOrderedMenu().getMenuId(),
                    createdOrderLineItem.getQuantity()
            );
            OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));

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

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    createdOrderLineItem.getOrderedMenu().getMenuId(),
                    createdOrderLineItem.getQuantity()
            );
            OrderRequest request = new OrderRequest(savedOrderTable.getId() + 1, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable emptyOrderTable = saveOrderTable(10, true);
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    createdOrderLineItem.getOrderedMenu().getMenuId(),
                    createdOrderLineItem.getQuantity()
            );

            OrderRequest request = new OrderRequest(emptyOrderTable.getId(), List.of(orderLineItemRequest));

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
                OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                        createdOrderLineItem.getOrderedMenu().getMenuId(),
                        createdOrderLineItem.getQuantity()
                );
                OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
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
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    createdOrderLineItem.getOrderedMenu().getMenuId(),
                    createdOrderLineItem.getQuantity()
            );
            OrderRequest createRequest = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
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
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    createdOrderLineItem.getOrderedMenu().getMenuId(),
                    createdOrderLineItem.getQuantity()
            );
            OrderRequest createRequest = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
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
            OrderTable orderTable = saveOrderTable(10, false);
            Order completionOrder = new Order(
                    orderTable.getId(), List.of(createdOrderLineItem), orderValidator
            );
            completionOrder.changeOrderStatus(OrderStatus.COMPLETION);

            Order savedOrder = orderRepository.save(completionOrder);

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

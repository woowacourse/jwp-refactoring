package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderUpdateStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private OrderTable orderTable;

    private Menu menu;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        OrderTable newOrderTable = OrderTable.of(null, 10, false);
        orderTable = orderTableRepository.save(newOrderTable);
        menuGroup = menuGroupRepository.save(MenuGroup.from("후라이드 세트"));
        Product product = productRepository.save(Product.of("후라이드", 15_000L));
        Menu newMenu = Menu.of("치킨", 15_000L, menuGroup, List.of(MenuProduct.of(product, 1)));
        menu = menuRepository.save(newMenu);
    }

    @Nested
    class 주문_생성 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest);

            // when
            OrderResponse orderResponse = orderService.create(orderRequest);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(orderResponse.getId()).isNotNull();
                        softly.assertThat(orderResponse.getOrderLineItems().get(0).getQuantity()).isPositive();
                        softly.assertThat(orderResponse.getOrderLineItems().get(0).getQuantity())
                                .isEqualTo(orderLineItemRequest.getQuantity());
                    }
            );
        }

        @Test
        void 주문_상품이_1개_미만이면_예외_발생() {
            // given
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(orderRequest)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_아이템의_메뉴가_존재하지_않으면_예외_발생() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(null, 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(orderRequest)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외_발생() {
            // given
            long invalidOrderTableId = -1L;
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderCreateRequest request = createOrderRequest(invalidOrderTableId, COOKING, orderLineItemRequest);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 주문_테이블이_비어_있으면_예외_발생() {
            // given
            OrderTable newOrderTable = OrderTable.of(null, 10, true);
            OrderTable orderTable = orderTableRepository.save(newOrderTable);

            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(orderRequest)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 전체_주문_조회 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest);
            OrderResponse orderResponse = orderService.create(orderRequest);

            // when
            List<OrderResponse> orderResponses = orderService.readAll();

            // then
            assertThat(orderResponses)
                    .extracting(OrderResponse::getId)
                    .contains(orderResponse.getId());
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest);
            OrderResponse orderResponse = orderService.create(orderRequest);
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(MEAL);

            // when
            OrderResponse response = orderService.changeOrderStatus(orderResponse.getId(), request);

            // then
            assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus().name());
        }

        @Test
        void 주문이_존재하지_않으면_예외_발생() {
            // given
            Long invalidOrderId = -1L;
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(MEAL);

            // when, then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(invalidOrderId, request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 주문이_상태가_COMPLETION이면_예외_발생() {
            // given
            Order order = Order.of(orderTable, COMPLETION.name(), LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);

            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(MEAL);

            // when, then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(savedOrder.getId(), request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderCreateRequest createOrderRequest(final Long orderTableId,
                                                  final OrderStatus status,
                                                  final OrderLineItemRequest... orderLineItemRequests) {
        return new OrderCreateRequest(orderTableId, status, Arrays.asList(orderLineItemRequests));
    }
}

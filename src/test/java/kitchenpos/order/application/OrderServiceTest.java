package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateStatusRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
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

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        OrderTable newOrderTable = new OrderTable(null, 10, false);
        orderTable = orderTableRepository.save(newOrderTable);
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드 세트"));
        Product product = productRepository.save(Product.of("후라이드", BigDecimal.valueOf(15_000L)));
        MenuProduct menuProduct = new MenuProduct(null, product, 1L);
        Menu newMenu = Menu.of("치킨", BigDecimal.valueOf(15_000L), menuGroup.getId(), List.of(menuProduct));
        menu = menuRepository.save(newMenu);
        orderLineItems = List.of(new OrderLineItem(null, menu, 1L), new OrderLineItem(null, menu, 1L));
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
            final Long wrongMenuId = -1L;
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(wrongMenuId, 1L);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(wrongMenuId, 1L);
            OrderCreateRequest orderRequest = createOrderRequest(orderTable.getId(), COOKING, orderLineItemRequest,
                    orderLineItemRequest2);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(orderRequest)
            ).isInstanceOf(NoSuchElementException.class);
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
            OrderTable newOrderTable = new OrderTable(null, 10, true);
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
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest("MEAL");

            // when
            OrderResponse response = orderService.changeOrderStatus(orderResponse.getId(), request);

            // then
            assertThat(response.getOrderStatus().name()).isEqualTo(request.getOrderStatus());
        }

        @Test
        void 주문이_존재하지_않으면_예외_발생() {
            // given
            Long invalidOrderId = -1L;
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest("MEAL");

            // when, then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(invalidOrderId, request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 주문이_상태가_COMPLETION이면_예외_발생() {
            // given
            Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);
            Order savedOrder = orderRepository.save(order);
            savedOrder.updateOrderStatus(COMPLETION.name());

            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest("MEAL");

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

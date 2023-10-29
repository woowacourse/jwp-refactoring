package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.validator.OrderValidator;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.UpdateOrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderValidator orderValidator;

    @Test
    void 주문을_저장한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final OrderRequest orderRequest = new OrderRequest(
                9L,
                orderLineItemRequests
        );

        final OrderResponse created = orderService.create(orderRequest);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 주문_상품들이_비어있으면_예외를_발생한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        final OrderRequest orderRequest = new OrderRequest(
                9L,
                orderLineItemRequests
        );

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴개수와_주무항목개수가_맞지_않으면_예외를_발생한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(100L, 5));
        final OrderRequest orderRequest = new OrderRequest(
                9L,
                orderLineItemRequests
        );

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_존재하지_않으면_예외를_발생한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final OrderRequest orderRequest = new OrderRequest(
                100L,
                orderLineItemRequests
        );

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문을_조회한다() {
        final List<Order> allOrder = orderRepository.findAll();
        final List<OrderResponse> expected = allOrder.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        List<OrderResponse> actual = orderService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 주문_상태를_변경한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest("COMPLETION");

        final OrderResponse orderResponse = orderService.changeOrderStatus(1L, updateOrderStatusRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @Test
    void 주문_상태_변경_시_주문이_없으면_예외를_발생한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest("COMPLETION");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, updateOrderStatusRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경_시_이미_완료된_주문이면_예외를_발생한다() {
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest("COMPLETION");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, updateOrderStatusRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}

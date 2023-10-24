package kitchenpos.application;

import fixture.OrderBuilder;
import fixture.OrderTableBuilder;
import fixture.TableGroupBuilder;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.UpdateOrderStatusRequest;
import kitchenpos.ui.response.OrderResponse;
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
        final List<Order> all = orderRepository.findAll();
        final List<OrderResponse> expected = all.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        List<OrderResponse> actual = orderService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 주문_상태를_변경한다() {
        final TableGroup tableGroup = TableGroupBuilder.init()
                .build();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTable orderTable = OrderTableBuilder.init()
                .empty(false)
                .tableGroup(tableGroup)
                .build();
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = OrderBuilder.init()
                .orderTable(savedOrderTable)
                .orderStatus(OrderStatus.MEAL)
                .build();
        orderRepository.save(order);
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
        final TableGroup tableGroup = TableGroupBuilder.init()
                .build();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTable orderTable = OrderTableBuilder.init()
                .empty(false)
                .tableGroup(savedTableGroup)
                .build();
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = OrderBuilder.init()
                .orderTable(savedOrderTable)
                .orderStatus(OrderStatus.MEAL)
                .build();
        orderRepository.save(order);
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest("COMPLETION");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, updateOrderStatusRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경_시_이미_완료된_주문이면_예외를_발생한다() {
        final TableGroup tableGroup = TableGroupBuilder.init()
                .build();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTable orderTable = OrderTableBuilder.init()
                .empty(false)
                .tableGroup(savedTableGroup)
                .build();
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = OrderBuilder.init()
                .orderTable(savedOrderTable)
                .orderStatus(OrderStatus.COMPLETION)
                .build();
        orderRepository.save(order);
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 3));
        orderLineItemRequests.add(new OrderLineItemRequest(2L, 2));
        orderLineItemRequests.add(new OrderLineItemRequest(3L, 5));
        final UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest("COMPLETION");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, updateOrderStatusRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}

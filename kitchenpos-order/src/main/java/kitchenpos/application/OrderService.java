package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemInOrderDto;
import kitchenpos.dto.OrderResponse;
import kitchenpos.event.ValidateExistMenuEvent;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemInOrderDto> orderLineItemInOrderDtos = request.getOrderLineItems();
        final List<OrderLineItem> orderLineItems = orderLineItemInOrderDtos.stream()
                .map(this::convertOrderLineItem)
                .collect(Collectors.toList());

        final OrderTable orderTable = findOrderTable(request.getOrderTableId());

        final Order order = new Order(orderTable, OrderStatus.COOKING, orderLineItems);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private OrderLineItem convertOrderLineItem(final OrderLineItemInOrderDto request) {
        final Long menuId = request.getMenuId();
        publisher.publishEvent(new ValidateExistMenuEvent(menuId));
        return new OrderLineItem(menuId, request.getMenuName(), request.getMenuPrice(), request.getQuantity());
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문 테이블입니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order findOrder = findOrder(orderId);
        findOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(findOrder);
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문입니다."));
    }
}

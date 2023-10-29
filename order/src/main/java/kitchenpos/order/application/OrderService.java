package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateResponse;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemsMapper orderLineItemsMapper;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemsMapper orderLineItemsMapper,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemsMapper = orderLineItemsMapper;
        this.orderValidator = orderValidator;
    }

    public OrderCreateResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = orderLineItemsMapper.mapFrom(request.getOrderLineItems());

        final Order order = Order.of(request.getOrderTableId(), OrderStatus.COOKING, orderLineItems, orderValidator);
        final Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = findOrderById(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus().name());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. id = " + orderId));
    }
}

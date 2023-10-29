package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderMapper;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderUpdateStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator,
                        final OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final Order order = orderMapper.toOrder(request);
        orderValidator.validate(order);
        return OrderResponse.toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        orderValidator.validateOrderStatus(savedOrder.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.toResponse(orderRepository.save(savedOrder));
    }
}

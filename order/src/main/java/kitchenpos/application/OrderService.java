package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemQuantityDto;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderUpdateStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemMapper orderLineItemMapper;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderLineItemMapper orderLineItemMapper,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemMapper = orderLineItemMapper;
        this.orderValidator = orderValidator;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemQuantityDto> orderLineItemQuantities = orderCreateRequest.toOrderLineItemQuantities();
        Order order = new Order(
                orderCreateRequest.getOrderTableId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                orderLineItemQuantities,
                orderLineItemMapper,
                orderValidator
        );
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderUpdateStatusRequest orderRequest) {
        Order order = getOrder(orderId);
        OrderStatus orderStatus = OrderStatus.from(orderRequest.getOrderStatus());
        order.updateStatus(orderStatus);
        return OrderResponse.from(order);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}

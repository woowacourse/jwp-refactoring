package kitchenpos.order.service;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderMapper.mapFrom(orderRequest);
        order.register(orderValidator);
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest statusRequest) {
        final Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.valueOf(statusRequest.getOrderStatus()));
        return OrderResponse.of(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

package core.application;

import core.application.dto.OrderChangeStatusRequest;
import core.application.dto.OrderRequest;
import core.application.dto.OrderResponse;
import core.domain.Order;
import core.domain.OrderRepository;
import core.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;

    public OrderService(
            final OrderRepository orderRepository,
            OrderValidator orderValidator, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        Order order = orderMapper.toOrder(request, orderValidator);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
        order.changeOrderStatus(request.getOrderStatus());
        orderRepository.save(order);
        return OrderResponse.from(order);
    }
}

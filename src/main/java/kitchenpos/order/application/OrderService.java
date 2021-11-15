package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    public OrderResponse create(OrderRequest request) {
        Order order = request.toEntity();
        orderValidator.validate(order);
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponse.listOf(orders);
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}

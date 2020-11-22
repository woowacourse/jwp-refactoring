package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderCreateService;
import kitchenpos.domain.OrderRepository;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderCreateService orderCreateService;

    public OrderService(OrderRepository orderRepository, OrderCreateService orderCreateService) {
        this.orderRepository = orderRepository;
        this.orderCreateService = orderCreateService;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        Order order = orderCreateService.create(orderCreateRequest.getOrderTableId(),
            orderCreateRequest.getOrderLineItems());
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());
        return OrderResponse.of(order);
    }
}

package kitchenpos.order.service;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.dto.response.OrdersResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(
            final OrderMapper orderMapper,
            final OrderValidator orderValidator,
            final OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        Order order = orderMapper.mappingToOrder(request);
        order.validate((it) -> orderValidator.validateExistInOrderTable((Order) it));
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public OrdersResponse list() {
        List<Order> orders = orderRepository.findAll();
        return OrdersResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}

package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderValidator orderValidator,
                        OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderCommand orderCommand) {
        OrderLineItems orderLineItems = new OrderLineItems(orderCommand.toEntity());

        return OrderResponse.from(orderRepository.save(
                Order.startCooking(orderCommand.getOrderTableId(), orderLineItems, orderValidator)));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String status) {
        Order savedOrder = getOrder(orderId);
        savedOrder.changeStatus(OrderStatus.valueOf(status));
        return OrderResponse.from(savedOrder);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}

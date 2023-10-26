package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = createOrderLineItemsByOrderRequest(orderRequest);
        final Order order = Order.of(orderRequest.getOrderTableId(), orderLineItems, orderValidator);
        return orderRepository.save(order);
    }

    private static List<OrderLineItem> createOrderLineItemsByOrderRequest(final OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return savedOrder;
    }
}

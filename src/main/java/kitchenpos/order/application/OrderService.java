package kitchenpos.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.application.mapper.OrderMapper;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(request.getOrderLineItems());
        final Order order = OrderMapper.toOrder(request.getOrderTableId(), orderLineItems);
        orderValidator.validate(order);
        orderRepository.save(order);

        return OrderMapper.toOrderResponse(order);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(request -> new OrderLineItem(request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> readAll() {
        final List<Order> orders = orderRepository.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(
            final Long orderId,
            final OrderUpdateStatusRequest request
    ) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
        order.updateOrderStatus(request.getOrderStatus());

        return OrderMapper.toOrderResponse(order);
    }
}

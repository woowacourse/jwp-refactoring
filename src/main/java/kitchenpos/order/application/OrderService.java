package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
        OrderRepository orderRepository,
        OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        orderValidator.validateOrder(request);

        Order order = new Order(request.getOrderTableId());
        OrderLineItems orderLineItems = generateOrderLineItems(order, request.getOrderLineItems());
        order.addOrderLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItems generateOrderLineItems(Order order, List<OrderLineItemRequest> requests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemRequest request : requests) {
            OrderLineItem orderLineItem = new OrderLineItem(request.getQuantity(), order, request.getMenuId());
            orderLineItems.add(orderLineItem);
        }

        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::from)
            .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.findByName(request.getOrderStatus()));

        return OrderResponse.from(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(String.format("%s ID의 Order가 없습니다.", orderId)));
    }
}

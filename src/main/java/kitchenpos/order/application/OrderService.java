package kitchenpos.order.application;

import kitchenpos.order.application.dto.request.CreateOrderRequest;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest createOrderRequest) {
        Order order = new Order(createOrderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = extractOrderLineItems(createOrderRequest);

        orderValidator.validate(order, orderLineItems);

        orderRepository.save(order);
        saveOrderLineItems(order, orderLineItems);
        return OrderResponse.of(order, orderLineItems);
    }

    private List<OrderLineItem> extractOrderLineItems(CreateOrderRequest createOrderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = createOrderRequest.getOrderLineItems();
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private void saveOrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(order);
            orderLineItemRepository.save(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> response = new ArrayList<>();
        for (Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            response.add(OrderResponse.of(order, orderLineItems));
        }
        return response;
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (order.isCompleted()) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(updateOrderStatusRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        return OrderResponse.of(order, orderLineItems);
    }
}

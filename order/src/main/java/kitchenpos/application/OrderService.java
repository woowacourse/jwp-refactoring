package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;
    private final OrderLineItemValidator orderLineItemValidator;
    private final OrderMapper orderMapper;
    private final OrderLineItemsMapper orderLineItemsMapper;

    public OrderService(
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderValidator orderValidator,
            OrderLineItemValidator orderLineItemValidator,
            OrderMapper orderMapper, OrderLineItemsMapper orderLineItemsMapper) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
        this.orderLineItemValidator = orderLineItemValidator;
        this.orderMapper = orderMapper;
        this.orderLineItemsMapper = orderLineItemsMapper;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest createOrderRequest) {
        Order order = orderMapper.toOrder(createOrderRequest.getOrderTableId(), orderValidator);
        List<OrderLineItem> orderLineItems = orderLineItemsMapper.toOrderLineItems(createOrderRequest, orderLineItemValidator);

        orderRepository.save(order);
        saveOrderLineItems(order, orderLineItems);
        return OrderResponse.of(order, orderLineItems);
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

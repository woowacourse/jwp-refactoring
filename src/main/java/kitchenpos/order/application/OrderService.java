package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.request.OrderCommand;
import kitchenpos.order.application.request.OrderLineItemCommand;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenuCreator;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderMenuCreator orderMenuCreator;

    public OrderService(OrderValidator orderValidator,
                        OrderRepository orderRepository,
                        OrderMenuCreator orderMenuCreator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderMenuCreator = orderMenuCreator;
    }

    @Transactional
    public OrderResponse create(OrderCommand orderCommand) {
        return OrderResponse.from(orderRepository.save(
                        Order.startCooking(
                                orderCommand.getOrderTableId(),
                                new OrderLineItems(newOrderLIneItems(orderCommand)),
                                orderValidator)
                )
        );
    }

    private List<OrderLineItem> newOrderLIneItems(OrderCommand orderCommand) {
        return orderCommand.getOrderLineItems().stream()
                .map(this::newOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem newOrderLineItem(OrderLineItemCommand orderLineItem) {
        return OrderLineItem.create(orderLineItem.getMenuId(), orderLineItem.getQuantity(), orderMenuCreator);
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

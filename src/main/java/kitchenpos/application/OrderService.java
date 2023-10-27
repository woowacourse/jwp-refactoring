package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderLineItemsRequest;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final List<OrderCreationValidator> orderCreationValidators;

    public OrderService(final OrderRepository orderRepository, final List<OrderCreationValidator> orderCreationValidators) {
        this.orderRepository = orderRepository;
        this.orderCreationValidators = orderCreationValidators;
    }

    @Transactional
    public OrderResponse create(final Long orderTableId, final List<OrderLineItemsRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = convertToLineItems(orderLineItemRequests);

        final Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        for (OrderCreationValidator orderCreationValidator : orderCreationValidators) {
            orderCreationValidator.validate(order);
        }

        return OrderResponse.from(orderRepository.save(order));
    }

    private static List<OrderLineItem> convertToLineItems(final List<OrderLineItemsRequest> orderLineItemRequests) {
        return orderLineItemRequests
                .stream().map(OrderLineItemsRequest::toEntity)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order savedOrder = orderRepository.findMandatoryById(orderId);
        savedOrder.transitionToNextStatus();
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}

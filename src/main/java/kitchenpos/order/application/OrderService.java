package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderLineItemsRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final List<OrderCreationValidator> orderCreationValidators;

    public OrderService(final OrderDao orderDao, final List<OrderCreationValidator> orderCreationValidators) {
        this.orderDao = orderDao;
        this.orderCreationValidators = orderCreationValidators;
    }

    @Transactional
    public OrderResponse create(final Long orderTableId, final List<OrderLineItemsRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = convertToLineItems(orderLineItemRequests);

        final Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        for (OrderCreationValidator orderCreationValidator : orderCreationValidators) {
            orderCreationValidator.validate(order);
        }

        return OrderResponse.from(orderDao.save(order));
    }

    private static List<OrderLineItem> convertToLineItems(final List<OrderLineItemsRequest> orderLineItemRequests) {
        return orderLineItemRequests
                .stream().map(OrderLineItemsRequest::toEntity)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findMandatoryById(orderId);
        savedOrder.transitionStatusTo(orderStatus);
        orderDao.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}

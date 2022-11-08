package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderCreateResponse;
import kitchenpos.order.dto.OrderFindResponse;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderLineItemsValidateEvent;
import kitchenpos.order.dto.OrderStatusChangeResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableValidator orderTableValidator;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao,
                        final OrderTableValidator orderTableValidatorImpl,
                        final ApplicationEventPublisher eventPublisher) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableValidator = orderTableValidatorImpl;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderCreateResponse create(final OrderCreateRequest orderCreateRequest) {
        eventPublisher.publishEvent(new OrderLineItemsValidateEvent(orderCreateRequest));

        final Long orderTableId = orderTableValidator.validOrderTableAndGet(orderCreateRequest);

        final Order savedOrder = orderDao.save(
                new Order(
                        orderTableId,
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now()
                )
        );
        final List<OrderLineItemCreateRequest> orderLineItems = orderCreateRequest.getOrderLineItems();
        final List<OrderLineItem> savedOrderLineItems = generateOrderLineItems(savedOrder.getId(), orderLineItems);
        savedOrder.addOrderLineItem(savedOrderLineItems);

        return OrderCreateResponse.from(savedOrder);
    }

    private List<OrderLineItem> generateOrderLineItems(final Long orderId,
                                                       final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItem : orderLineItems) {
            savedOrderLineItems.add(
                    orderLineItemDao.save(
                            new OrderLineItem(
                                    null,
                                    orderId,
                                    orderLineItem.getQuantity()
                            )
                    )
            );
        }
        return savedOrderLineItems;
    }

    public List<OrderFindResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> {
                    final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
                    order.addOrderLineItem(orderLineItems);
                    return OrderFindResponse.from(order);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderStatusChangeResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateChangableStatus(savedOrder);
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.update(savedOrder);
        return new OrderStatusChangeResponse(savedOrder.getOrderStatus());
    }

    private void validateChangableStatus(final Order savedOrder) {
        if (savedOrder.isCompletionOrder()) {
            throw new IllegalArgumentException();
        }
    }
}

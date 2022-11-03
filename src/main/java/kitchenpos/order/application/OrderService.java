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
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao,
                        final OrderTableDao orderTableDao, final ApplicationEventPublisher eventPublisher) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderCreateResponse create(final OrderCreateRequest orderCreateRequest) {
        eventPublisher.publishEvent(new OrderLineItemsValidateEvent(orderCreateRequest));

        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateIfTableEmpty(orderTable);

        final Order savedOrder = orderDao.save(
                new Order(
                        orderTable.getId(),
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now()
                )
        );
        final List<OrderLineItemCreateRequest> orderLineItems = orderCreateRequest.getOrderLineItems();
        final List<OrderLineItem> savedOrderLineItems = generateOrderLineItems(orderTable, orderLineItems);
        savedOrder.addOrderLineItem(savedOrderLineItems);

        return OrderCreateResponse.from(savedOrder);
    }

    private List<OrderLineItem> generateOrderLineItems(final OrderTable orderTable,
                                                       final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItem : orderLineItems) {
            savedOrderLineItems.add(
                    orderLineItemDao.save(
                            new OrderLineItem(
                                    null,
                                    orderTable.getId(),
                                    orderLineItem.getQuantity()
                            )
                    )
            );
        }
        return savedOrderLineItems;
    }

    private void validateIfTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderFindResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(OrderFindResponse::from)
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

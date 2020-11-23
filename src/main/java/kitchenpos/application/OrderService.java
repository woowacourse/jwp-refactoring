package kitchenpos.application;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final OrderVerifier orderVerifier;

    public OrderService(
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableDao orderTableDao,
        final OrderVerifier orderVerifier
    ) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.orderVerifier = orderVerifier;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = orderCreateRequest.getOrderLineItems()
            .stream()
            .map(OrderLineItemCreateRequest::toEntity)
            .collect(Collectors.toList());

        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final Order order = orderDao.save(orderVerifier.toOrder(orderTable, orderLineItems));

        List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
            .peek(it -> it.changeOrderId(order.getId()))
            .map(orderLineItemDao::save)
            .collect(Collectors.toList());

        return OrderResponse.of(order, savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        Map<Long, Order> allOrders = orderDao.findAll()
            .stream()
            .collect(Collectors.toMap(Order::getId, it -> it));
        Map<Order, List<OrderLineItem>> orderLineItemsGroup = orderLineItemDao
            .findAllByOrderIdIn(allOrders.keySet()).stream()
            .collect(groupingBy(it -> allOrders.get(it.getOrderId())));

        return allOrders.values()
            .stream()
            .map(it -> OrderResponse.of(
                it,
                orderLineItemsGroup.getOrDefault(it, Collections.emptyList())
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId,
        final OrderChangeOrderStatusRequest orderChangeOrderStatusRequest
    ) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderChangeOrderStatusRequest.getOrderStatus());

        return OrderResponse.of(
            orderDao.save(savedOrder),
            orderLineItemDao.findAllByOrderId(savedOrder.getId())
        );
    }
}

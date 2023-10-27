package com.kitchenpos.application;

import com.kitchenpos.application.dto.OrderCreateRequest;
import com.kitchenpos.application.dto.OrderLineItemCreateRequest;
import com.kitchenpos.application.dto.OrderUpdateRequest;
import com.kitchenpos.domain.Order;
import com.kitchenpos.domain.OrderLineItem;
import com.kitchenpos.domain.OrderRepository;
import com.kitchenpos.domain.OrderStatus;
import com.kitchenpos.event.message.ValidatorMenuExist;
import com.kitchenpos.event.message.ValidatorOrderTable;
import com.kitchenpos.exception.OrderLineItemEmptyException;
import com.kitchenpos.exception.OrderNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private static final List<String> PROGRESS_MEAL_STATUS = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(final OrderRepository orderRepository, final ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        validateOrderLineItemsEmpty(req);
        publisher.publishEvent(new ValidatorOrderTable(req.getOrderTableId()));

        List<OrderLineItem> orderLineItems = makeOrderLineItems(req.getOrderLineItems());
        validateOrderLineItemEmpty(req, orderLineItems);

        Order order = Order.createDefault(req.getOrderTableId(), orderLineItems);
        return orderRepository.save(order);
    }

    private void validateOrderLineItemsEmpty(final OrderCreateRequest req) {
        if (req.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemEmptyException();
        }
    }

    private List<OrderLineItem> makeOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        orderLineItemRequests.forEach(orderLineItemRequest -> {
            publisher.publishEvent(new ValidatorMenuExist(orderLineItemRequest.getMenuId()));
            orderLineItems.add(orderLineItemRequest.toDomain());
        });

        return orderLineItems;
    }

    private void validateOrderLineItemEmpty(final OrderCreateRequest req, final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != req.getOrderLineItems().size()) {
            throw new OrderLineItemEmptyException();
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest req) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(req.getOrderStatus()).name());

        return savedOrder;
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean isStatusInProgressMealByIds(final List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, PROGRESS_MEAL_STATUS);
    }
}

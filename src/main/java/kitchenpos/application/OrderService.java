package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    @Transactional
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        Order newOrder = order.toBuilder()
            .id(null)
            .build();

        final OrderTable orderTable = orderTableRepository.findById(newOrder.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        newOrder = newOrder.toBuilder()
            .orderTableId(orderTable.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();

        final Order savedOrder = orderRepository.save(newOrder);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem newOrderLineItem = orderLineItem.toBuilder()
                .orderId(orderId)
                .build();
            savedOrderLineItems.add(orderLineItemRepository.save(newOrderLineItem));
        }

        return savedOrder.toBuilder()
            .orderLineItems(savedOrderLineItems)
            .build();
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<Order> result = new ArrayList<>();

        for (final Order order : orders) {
            Order newOrder = order.toBuilder()
                .orderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()))
                .build();
            result.add(newOrder);
        }

        return result;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());

        final Order newOrder = savedOrder.toBuilder()
            .orderStatus(orderStatus.name())
            .build();
        orderRepository.save(newOrder);

        return newOrder.toBuilder()
            .orderLineItems(orderLineItemRepository.findAllByOrderId(orderId))
            .build();
    }
}

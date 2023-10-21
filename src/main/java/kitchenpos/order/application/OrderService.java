package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        final List<OrderCreateRequest.OrderLineItemCreate> orderLineItemCreates = request.getOrderLineItemCreates();

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (CollectionUtils.isEmpty(orderLineItemCreates)) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
        final Order savedOrder = orderRepository.save(order);

        for (final OrderCreateRequest.OrderLineItemCreate orderLineItem : orderLineItemCreates) {
            savedOrder.addOrderLineItem(orderLineItemRepository.save(new OrderLineItem(savedOrder, menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(IllegalArgumentException::new), orderLineItem.getQuantity())));
        }

        return savedOrder.getId();
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        final List<OrderResponse> orderResponses = new ArrayList<>();
        for (final Order order : orders) {
            final List<Long> orderLineItemIds = order.getOrderLineItems()
                    .stream()
                    .map(OrderLineItem::getSeq)
                    .collect(Collectors.toList());
            orderResponses.add(new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(), order.getOrderedTime(), orderLineItemIds));
        }

        return orderResponses;
    }

    @Transactional
    public Long changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);
        orderRepository.save(savedOrder);

        return orderId;
    }
}

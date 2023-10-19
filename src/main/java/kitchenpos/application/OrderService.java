package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaOrderLineItemRepository;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final JpaMenuRepository jpaMenuRepository;
    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderLineItemRepository jpaOrderLineItemRepository;
    private final JpaOrderTableRepository jpaOrderTableRepository;

    public OrderService(
            final JpaMenuRepository jpaMenuRepository,
            final JpaOrderRepository jpaOrderRepository,
            final JpaOrderLineItemRepository jpaOrderLineItemRepository,
            final JpaOrderTableRepository jpaOrderTableRepository
    ) {
        this.jpaMenuRepository = jpaMenuRepository;
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaOrderLineItemRepository = jpaOrderLineItemRepository;
        this.jpaOrderTableRepository = jpaOrderTableRepository;
    }

    @Transactional
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.getMenu() != null)
                .map(orderLineItem -> orderLineItem.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != jpaMenuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        order.setId(null);

        if (order.getOrderTable() == null){
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = jpaOrderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = jpaOrderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(jpaOrderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = jpaOrderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(jpaOrderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = jpaOrderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus().name())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = order.getOrderStatus();
        savedOrder.setOrderStatus(orderStatus);

        jpaOrderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(jpaOrderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }
}

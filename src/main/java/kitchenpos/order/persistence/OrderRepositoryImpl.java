package kitchenpos.order.persistence;

import kitchenpos.order.application.entity.OrderEntity;
import kitchenpos.order.application.entity.OrderLineItemEntity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepositoryImpl(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final OrderEntity savedOrder = orderDao.save(OrderEntity.from(entity));

        final OrderLineItems savedOrderLineItems = saveOrderLineItems(entity, savedOrder);
        return savedOrder.toOrder(savedOrderLineItems);
    }

    private OrderLineItems saveOrderLineItems(final Order entity, final OrderEntity savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : entity.getOrderLineItems().getOrderLineItems()) {
            savedOrderLineItems.add(
                    orderLineItemDao.save(OrderLineItemEntity.of(savedOrder.getId(), new OrderLineItem(savedOrder.getId(), orderLineItem.getMenuId(),
                            orderLineItem.getQuantity()))
                    ).toOrderLineItem());
        }
        return new OrderLineItems(savedOrderLineItems);
    }

    @Override
    public Optional<Order> findById(final Long id) {
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(id)
                .stream()
                .map(OrderLineItemEntity::toOrderLineItem)
                .collect(Collectors.toList());
        return orderDao.findById(id).map(orderEntity -> orderEntity.toOrder(new OrderLineItems(orderLineItems)));
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll()
                .stream()
                .map(OrderEntity::toOrder)
                .collect(Collectors.toList());

        final List<Order> result = new ArrayList<>();
        for (final Order order : orders) {
            final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId())
                    .stream()
                    .map(OrderLineItemEntity::toOrderLineItem)
                    .collect(Collectors.toList());
            result.add(new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                    order.getOrderedTime(), new OrderLineItems(orderLineItems)));
        }

        return result;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                        final List<OrderStatus> orderStatuses) {
        final List<String> mappedOrderStatuses = orderStatuses.stream()
                .map(OrderStatus::name)
                .collect(Collectors.toList());
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, mappedOrderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<OrderStatus> orderStatuses) {
        final List<String> mappedOrderStatuses = orderStatuses.stream()
                .map(OrderStatus::name)
                .collect(Collectors.toList());
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, mappedOrderStatuses);
    }
}

package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

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
        final Order savedOrder = orderDao.save(entity);
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            final OrderLineItem savedOrderLineItem = orderLineItemDao.save(
                    new OrderLineItem(savedOrder.getId(), orderLineItem.getMenuId(), orderLineItem.getQuantity()));
            savedOrderLineItems.add(savedOrderLineItem);
        }
        return new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrderLineItems
        );
    }

    @Override
    public Optional<Order> findById(final Long id) {
        final Optional<Order> order = orderDao.findById(id);
        order.ifPresent(it -> it.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.get().getId())));
        return order;
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> new Order(
                                order.getId(),
                                order.getOrderTableId(),
                                order.getOrderStatus(),
                                order.getOrderedTime(),
                                orderLineItemDao.findAllByOrderId(order.getId())
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}

package kitchenpos.order.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;

    private final OrderLineItemDao orderLineItemDao;

    private final MenuDao menuDao;

    public OrderRepositoryImpl(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao,
                               final MenuDao menuDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.menuDao = menuDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order savedOrder = orderDao.save(entity);
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            final Menu menu = menuDao.findById(orderLineItem.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            final OrderLineItem savedOrderLineItem = orderLineItemDao.save(
                    new OrderLineItem(
                            savedOrder.getId(),
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            orderLineItem.getQuantity()
                    )
            );
            savedOrderLineItems.add(savedOrderLineItem);
        }
        return new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                new OrderLineItems(savedOrderLineItems)
        );
    }

    @Override
    public Optional<Order> findById(final Long id) {
        final Optional<Order> order = orderDao.findById(id);
        return order.map(
                it -> it.setOrderLineItems(new OrderLineItems(orderLineItemDao.findAllByOrderId(order.get().getId()))));
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> order.setOrderLineItems(
                        new OrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()))))
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

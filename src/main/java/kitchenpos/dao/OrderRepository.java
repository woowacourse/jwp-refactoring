package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao orderDao;
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao orderDao, final JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order savedOrder = orderDao.save(entity);

        final List<OrderLineItem> orderLineItems = savedOrder.getOrderLineItems();

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(savedOrder.getId());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        //TODO : 이곳 엄청 불편하다.. 다른 방법이 필요하다.
        final Order order = orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(id);
        order.setOrderLineItems(orderLineItems);

        return Optional.of(order);
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
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

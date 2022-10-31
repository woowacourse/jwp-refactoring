package kitchenpos.dao;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

public interface OrderDao {
    Order save(Order entity);

    Order findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}

@Repository
class OrderRepository implements OrderDao {
    private final JdbcTemplateOrderDao orderDao;
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    OrderRepository(final JdbcTemplateOrderDao orderDao,
                    final JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Long orderId = entity.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        entity.setOrderLineItems(savedOrderLineItems);
        return orderDao.save(entity);
    }

    @Override
    public Order findById(final Long id) {
        final Order order = orderDao.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_ORDER_EXCEPTION));
        order.setOrderLineItems(orderLineItemDao.findAllByOrderId(id));
        return order;
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orderDao.findAll();
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

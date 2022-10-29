package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao orderDao, final JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

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

    public Order getById(final Long id) throws IllegalArgumentException {
        final Order order = orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(id);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                        final List<OrderStatus> orderStatuses) {
        final List<String> orderStatusNames = orderStatuses.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatusNames);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<OrderStatus> orderStatuses) {
        final List<String> orderStatusNames = orderStatuses.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatusNames);
    }
}

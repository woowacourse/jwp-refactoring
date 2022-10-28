package kitchenpos.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    Order changeOrderStatus(Long orderId, String orderStatus);
}

@Repository
class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    public OrderRepository(final JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao,
                           final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.jdbcTemplateOrderLineItemDao = jdbcTemplateOrderLineItemDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public Order save(final Order entity) {
        entity.setOrderStatus(OrderStatus.COOKING.name());
        entity.setOrderedTime(LocalDateTime.now());
        final Order savedOrder = jdbcTemplateOrderDao.save(entity);
        final List<OrderLineItem> orderLineItems = entity.getOrderLineItems();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(savedOrder.getId());
        }
        savedOrder.setOrderLineItems(orderLineItems);
        final OrderTable orderTable = jdbcTemplateOrderTableDao.findById(entity.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.setOrderTable(orderTable);
        return savedOrder;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        final List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(id);
        final Optional<Order> order = jdbcTemplateOrderDao.findById(id);
        order.ifPresent(it -> it.setOrderLineItems(orderLineItems));
        return order;
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = jdbcTemplateOrderDao.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public Order changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = jdbcTemplateOrderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
        savedOrder.setOrderStatus(orderStatus);
        jdbcTemplateOrderDao.save(savedOrder);
        savedOrder.setOrderLineItems(jdbcTemplateOrderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}


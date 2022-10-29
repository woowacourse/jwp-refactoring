package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.infrastructure.JdbcTemplateOrderDao;
import kitchenpos.infrastructure.JdbcTemplateOrderLineItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao ordersOrderLineItemDao;

    public OrderRepository(JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           JdbcTemplateOrderLineItemDao ordersOrderLineItemDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.ordersOrderLineItemDao = ordersOrderLineItemDao;
    }

    @Override
    public Order save(Order entity) {
        Order savedOrder = jdbcTemplateOrderDao.save(entity);

        List<OrderLineItem> orderLineItems = saveOrderLineItems(entity, savedOrder);

        savedOrder.setOrderLineItems(orderLineItems);
        return savedOrder;
    }

    private List<OrderLineItem> saveOrderLineItems(Order entity, Order savedOrder) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            OrderLineItem savedOrderLineItem = ordersOrderLineItemDao.save(
                    new OrderLineItem(
                            savedOrder.getId(),
                            orderLineItem.getOrderId(),
                            orderLineItem.getQuantity()
                    )
            );
            orderLineItems.add(savedOrderLineItem);
        }
        return orderLineItems;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jdbcTemplateOrderDao.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}

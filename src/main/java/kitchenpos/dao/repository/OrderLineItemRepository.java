package kitchenpos.dao.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepository implements OrderLineItemDao {
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    public OrderLineItemRepository(JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return orderLineItemDao.save(entity);
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return orderLineItemDao.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItemDao.findAll();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItemDao.findAllByOrderId(orderId);
    }
}

package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.repository.jdbc.JdbcTemplateOrderLineItemDao;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepositoryImpl implements OrderLineItemRepository {
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    public OrderLineItemRepositoryImpl(JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return orderLineItemDao.save(entity);
    }

    @Override
    public OrderLineItem findById(Long id) {
        return orderLineItemDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("주문 항목이 존재해야 한다."));
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

package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepositoryImpl implements OrderLineItemRepository {

    private final OrderLineItemDao orderLineItemDao;

    public OrderLineItemRepositoryImpl(final OrderLineItemDao orderLineItemDao) {
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        return orderLineItemDao.save(entity);
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return orderLineItemDao.findById(id);
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItemDao.findAll();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return orderLineItemDao.findAllByOrderId(orderId);
    }
}

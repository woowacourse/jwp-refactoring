package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;

    public OrderRepository(final JdbcTemplateOrderDao jdbcTemplateOrderDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
    }

    @Override
    public Order save(final Order entity) {
        return jdbcTemplateOrderDao.save(entity);
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return jdbcTemplateOrderDao.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplateOrderDao.findAll();
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

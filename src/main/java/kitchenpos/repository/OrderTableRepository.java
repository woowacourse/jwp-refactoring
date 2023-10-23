package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository implements OrderTableDao {

    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    public OrderTableRepository(final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        return jdbcTemplateOrderTableDao.save(entity);
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return jdbcTemplateOrderTableDao.findById(id);
    }

    @Override
    public List<OrderTable> findAll() {
        return jdbcTemplateOrderTableDao.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return jdbcTemplateOrderTableDao.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroupId);
    }
}

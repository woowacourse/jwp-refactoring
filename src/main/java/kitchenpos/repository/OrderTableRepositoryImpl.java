package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements  OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepositoryImpl(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        return orderTableDao.save(entity);
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return orderTableDao.findById(id);
    }

    @Override
    public List<OrderTable> findAll() {
        return orderTableDao.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return orderTableDao.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}

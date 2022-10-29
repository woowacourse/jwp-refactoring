package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepository(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public OrderTable save(OrderTable entity) {
        return orderTableDao.save(entity);
    }

    public Optional<OrderTable> findById(Long id) {
        return orderTableDao.findById(id);
    }

    public List<OrderTable> findAll() {
        return orderTableDao.findAll();
    }

    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTableDao.findAllByIdIn(ids);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}

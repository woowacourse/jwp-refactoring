package kitchenpos.table.dao;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableJdbcRepository implements OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableJdbcRepository(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return orderTableDao.findAllByIdIn(ids);
    }

    @Override
    public Long findTableGroupIdById(final Long id) {
        return orderTableDao.findTableGroupIdById(id);
    }
}

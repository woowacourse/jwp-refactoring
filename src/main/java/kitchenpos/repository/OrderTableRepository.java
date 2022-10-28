package kitchenpos.repository;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepository(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public OrderTable get(final Long id) {
        return orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

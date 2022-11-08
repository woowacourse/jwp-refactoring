package kitchenpos.domain.table;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepository(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    public OrderTable update(OrderTable orderTable) {
        return save(orderTable);
    }

    public OrderTable findById(Long id) {
        return orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public OrderTable save(OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> findAll() {
        return orderTableDao.findAll();
    }
}

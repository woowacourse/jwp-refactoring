package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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

    public void update(OrderTable orderTable) {
        orderTableDao.save(orderTable);
    }
}

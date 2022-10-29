package kitchenpos.repository;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderTableRepository(final OrderDao orderDao,
                                final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTable get(final Long id) {
        final var orderTable = orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final var orders = orderDao.findByOrderTableId(orderTable.getId());

        return new OrderTable(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty(),
                orders
        );
    }

    public OrderTable add(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> getAll() {
        return orderTableDao.findAll();
    }
}

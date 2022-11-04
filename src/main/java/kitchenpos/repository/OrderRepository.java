package kitchenpos.repository;

import java.util.List;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderRepository(final OrderDao orderDao,
                           final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order save(final Order entity) {
        OrderTable orderTable = orderTableDao.getById(entity.getOrderTableId());
        validateEmptyOrderTable(orderTable);

        return orderDao.save(new Order(orderTable.getId(), entity.getOrderLineItems()));
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Order getById(final Long id) {
        return orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> findAll() {
        return orderDao.findAll();
    }
}

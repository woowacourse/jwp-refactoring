package kitchenpos.dao.ordertable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.dao.order.JdbcTemplateOrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository implements OrderTableDao {

    private JdbcTemplateOrderTableDao orderTableDao;
    private JdbcTemplateOrderDao orderDao;

    public OrderTableRepository(final JdbcTemplateOrderTableDao orderTableDao,
                                final JdbcTemplateOrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Override
    public OrderTable save(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    @Override
    public Optional<OrderTable> findById(final Long orderTableId) {
        final Optional<OrderTable> orderTableWrap = orderTableDao.findById(orderTableId);
        if (orderTableWrap.isPresent()) {
            final Order order = orderDao.findByOrderTableId(orderTableId)
                    .orElseThrow(NoSuchElementException::new);
            final OrderTable orderTable = orderTableWrap.get();
            orderTable.changeOrderStatus(order.getOrderStatus());
        }
        return orderTableWrap;
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

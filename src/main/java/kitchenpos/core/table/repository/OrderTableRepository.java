package kitchenpos.core.table.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.table.domain.OrderTableDao;
import kitchenpos.core.order.repository.JdbcTemplateOrderDao;
import kitchenpos.core.table.domain.OrderTable;
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

        orderTableWrap.ifPresent(it -> changeOrderStatus(orderTableId, it));

//        if (orderTableWrap.isPresent()) {
//            changeOrderStatus(orderTableId, orderTableWrap);
//        }
        return orderTableWrap;
    }

    private void changeOrderStatus(final Long orderTableId, final OrderTable orderTable) {
        final Optional<Order> orderWrap = orderDao.findByOrderTableId(orderTableId);
        orderWrap.ifPresent(it -> orderTable.changeOrderStatus(it.getOrderStatus()));
//        if (orderWrap.isPresent()) {
//            final OrderTable orderTable = orderTableWrap.get();
//            orderTable.changeOrderStatus(orderWrap.get().getOrderStatus());
//        }
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

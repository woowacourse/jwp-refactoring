package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class OrderTableGroupRepository implements OrderTableGroupDao {

    private final OrderTableGroupDao orderTableGroupDao;
    private final OrderTableDao orderTableDao;

    public OrderTableGroupRepository(OrderTableGroupDao orderTableGroupDao, OrderTableDao orderTableDao) {
        this.orderTableGroupDao = orderTableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public OrderTableGroup save(OrderTableGroup entity) {
        OrderTableGroup orderTableGroup = orderTableGroupDao.save(entity);
        for (OrderTable orderTable : orderTableGroup.getOrderTables()) {
            orderTable.setTableGroupId(orderTableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return orderTableGroup;
    }

    @Override
    public Optional<OrderTableGroup> findById(Long id) {
        return orderTableGroupDao.findById(id);
    }

    @Override
    public List<OrderTableGroup> findAll() {
        return orderTableGroupDao.findAll();
    }
}

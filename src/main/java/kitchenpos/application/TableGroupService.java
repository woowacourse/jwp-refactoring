package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables savedOrderTables = orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds());
        return tableGroupDao.save(tableGroup.create(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = getOrderTables(tableGroupId);
        orderTableDao.saveAll(orderTables.ungroup());
    }

    private OrderTables getOrderTables(final Long tableGroupId) {
        final OrderTables orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTables.getOrderTableIds(),
                Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }
}

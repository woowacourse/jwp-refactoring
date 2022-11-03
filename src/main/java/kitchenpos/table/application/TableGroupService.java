package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderStatus.COOKING;
import static kitchenpos.table.domain.OrderStatus.MEAL;

import java.util.Arrays;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables savedOrderTables = orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds());
        return tableGroupDao.save(tableGroup.init(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = getOrderTables(tableGroupId);
        orderTableDao.saveAll(orderTables.ungroup());
    }

    private OrderTables getOrderTables(final Long tableGroupId) {
        final OrderTables orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        if (orderTableDao.existsByOrderTableIdInAndOrderStatusIn(orderTables.getOrderTableIds(),
                Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }
}

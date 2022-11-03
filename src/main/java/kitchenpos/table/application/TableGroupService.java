package kitchenpos.table.application;

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
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao, final OrderValidator orderValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables savedOrderTables = orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds());
        return tableGroupDao.save(tableGroup.init(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        orderValidator.ValidateOrderTableIds(orderTables.getOrderTableIds());
        orderTableDao.saveAll(orderTables.ungroup());
    }
}

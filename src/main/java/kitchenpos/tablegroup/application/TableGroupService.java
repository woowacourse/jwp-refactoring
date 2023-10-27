package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.ordertable.application.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.domain.OrderTableUngroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupCustomDao tableGroupDao;
    private final List<OrderTableUngroupValidator> orderTableUngroupValidators;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupCustomDao tableGroupDao,
                             final List<OrderTableUngroupValidator> orderTableUngroupValidators) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderTableUngroupValidators = orderTableUngroupValidators;
    }

    @Transactional
    public TableGroupResponse create(final List<Long> orderTableIds) {
        final OrderTables orderTables = new OrderTables(orderTableDao.findAllByIdIn(orderTableIds));
        validateOrderTables(orderTableIds, orderTables);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTables.getOrderTables()));
        return TableGroupResponse.from(tableGroupDao.save(tableGroup));
    }

    private static void validateOrderTables(final List<Long> orderTableIds, final OrderTables orderTables) {
        if (!orderTables.hasSize(orderTableIds.size())) {
            throw new IllegalArgumentException();
        }

        if (!orderTables.isPersistableInTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableDao.findAllByTableGroupId(tableGroupId));
        orderTables.ungroup(orderTableUngroupValidators);
        orderTableDao.saveAll(orderTables);
    }
}

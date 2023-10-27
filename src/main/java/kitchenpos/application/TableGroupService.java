package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupCustomDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupCustomDao tableGroupDao;
    private final List<OrderTableUpGroupValidator> orderTableUpGroupValidators;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupCustomDao tableGroupDao,
                             final List<OrderTableUpGroupValidator> orderTableUpGroupValidators) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderTableUpGroupValidators = orderTableUpGroupValidators;
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
        orderTables.ungroup(orderTableUpGroupValidators);
        orderTableDao.saveAll(orderTables);
    }
}

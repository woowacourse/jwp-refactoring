package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupService(
            final TableGroupDao tableGroupDao,
            final OrderTableDao orderTableDao
    ) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getParsedOrderTableIds();
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateDuplicate(orderTableIds, orderTables);

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup())
                .group(orderTables);

        orderTableDao.updateAll(tableGroup.getOrderTables());

        return TableGroupResponse.of(tableGroup);
    }

    private void validateDuplicate(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderCompletion(orderTables);

        final List<OrderTable> ungroupedTables = orderTables.stream()
                .map(OrderTable::ungroup)
                .collect(Collectors.toList());

        orderTableDao.updateAll(ungroupedTables);
    }

    private void validateOrderCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> tables = orderTableDao.findAllByIdIn(orderTableIds);
        for (final OrderTable table : tables) {
            table.checkCanUnGroup();
        }
    }
}

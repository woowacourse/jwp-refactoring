package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(
        final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao
    ) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
            .stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        validateOrderTablesCount(orderTableIds);

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateSavedTable(orderTableIds, savedOrderTables);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(savedOrderTables)
            .build();

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        orderTableDao.saveAll(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateOrderTablesCount(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSavedTable(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        validateOrderStatus(tableGroup.getOrderTables());

        tableGroup.ungroup();

        orderTableDao.saveAll(tableGroup.getOrderTables());
        tableGroupDao.save(tableGroup);
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        if (isAnyOrderTableInProgress(orderTables)){
            throw new IllegalArgumentException();
        }
    }

    private boolean isAnyOrderTableInProgress(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .anyMatch(OrderTable::isInProgress);
    }
}

package kitchenpos.core.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.core.table.domain.OrderTableDao;
import kitchenpos.core.table.domain.TableGroupDao;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroupRequest) {
        final List<OrderTable> orderTables = tableGroupRequest.getOrderTables();
        final List<OrderTable> savedOrderTables = getSavedOrderTables(orderTables);

        return tableGroupDao.save(TableGroup.of(
                LocalDateTime.now(),
                savedOrderTables)
        );
    }

    private List<OrderTable> getSavedOrderTables(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateExistOrderTables(orderTables, savedOrderTables);
        return savedOrderTables;
    }

    private void validateExistOrderTables(final List<OrderTable> orderTables, final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블로 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = getTableGroupWithOrderTables(tableGroupId);
        tableGroup.unGroup();
        tableGroupDao.save(tableGroup);
    }

    private TableGroup getTableGroupWithOrderTables(final Long tableGroupId) {
        final TableGroup tableGroup = getTableGroup(tableGroupId);
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        tableGroup.changeOrderTables(orderTables);
        return tableGroup;
    }

    private TableGroup getTableGroup(final Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}

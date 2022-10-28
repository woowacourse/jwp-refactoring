package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        validateOrderTablesSize(orderTables);
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(getOrderTableIds(orderTables));
        validateOrderTablesSize(orderTables, savedOrderTables);
        validateOrderTableAlreadyInGroup(savedOrderTables);
        tableGroup.updateCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        updateTableGroupOfOrderTables(savedOrderTables, savedTableGroup);
        return savedTableGroup;
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 테이블이 두 개 미만이거나 비어있을 수 없습니다.");
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables, final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("[ERROR] OrderTable 의 개수가 저장된 개수와 일치하지 않습니다.");
        }
    }

    private void validateOrderTableAlreadyInGroup(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("[ERROR] OrderTable 이미 단체로 지정되어 있습니다.");
            }
        }
    }

    private void updateTableGroupOfOrderTables(final List<OrderTable> savedOrderTables, final TableGroup savedTableGroup) {
        final Long savedTableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(savedTableGroupId);
            savedOrderTable.updateEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.addAllOrderTables(savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                getOrderTableIds(orderTables), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )) {
            throw new IllegalArgumentException("[ERROR] 단체 지정을 해지할 수 없습니다.");
        }
    }
}

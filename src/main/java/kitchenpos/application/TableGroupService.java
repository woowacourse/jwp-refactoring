package kitchenpos.application;

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
import kitchenpos.dto.TableGroupDto;
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
    public TableGroup create(final TableGroupDto tableGroupDto) {
        validateOrderTablesSize(tableGroupDto);
        tableGroupDto.updateCreatedDateToNow();
        TableGroup tableGroup = tableGroupDto.toEntity();
        validateOrderTableAlreadyInGroup(tableGroup);
        tableGroup.updateId(tableGroupDao.saveAndGetId(tableGroup));
        updateTableGroupOfOrderTables(tableGroup.getOrderTables(), tableGroup);
        return tableGroup;
    }

    private void validateOrderTablesSize(final TableGroupDto tableGroupDto) {
        final List<OrderTable> orderTables = tableGroupDto.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(getOrderTableIds(orderTables));
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블의 개수가 저장된 개수와 일치하지 않습니다.");
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTableAlreadyInGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블이 아닙니다.");
        }
    }

    private void updateTableGroupOfOrderTables(final List<OrderTable> savedOrderTables,
                                               final TableGroup savedTableGroup) {
        final Long savedTableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(savedTableGroupId);
            savedOrderTable.updateEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
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

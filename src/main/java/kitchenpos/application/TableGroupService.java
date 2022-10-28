package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        validateOrderTableSize(orderTableIds);

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateOrderTableSize(orderTableIds, savedOrderTables);
        validateOrderTableIsNotEmpty(savedOrderTables);

        TableGroup tableGroup = request.toTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        groupOrderTables(savedOrderTables, savedTableGroup);

        return new TableGroupResponse(savedTableGroup);
    }

    private void validateOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void groupOrderTables(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        Long tableGroupId = savedTableGroup.getId();
        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.joinTableGroup(tableGroupId);
            savedOrderTable.changeEmptyStatus(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.addOrderTables(savedOrderTables);
    }

    private void validateOrderTableSize(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIsNotEmpty(List<OrderTable> savedOrderTables) {
        for (OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.leaveTableGroup();
            orderTable.changeEmptyStatus(false);
            orderTableDao.save(orderTable);
        }
    }
}

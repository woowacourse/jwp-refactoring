package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tableGroup.CreateTableGroupRequest;

@Service
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
    public TableGroup create(final CreateTableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
            .map(table -> table.getId())
            .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않은 테이블입니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("이미 다른 그룹에 존재하는 테이블입니다.");
            }

            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 테이블입니다.");
            }
        }

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());
        final Long tableGroupId = savedTableGroup.getId();

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}

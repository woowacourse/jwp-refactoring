package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTableIdDto> orderTableIdsDto = request.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableIdsDto) || orderTableIdsDto.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTableIdsDto.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIdsDto.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(savedTableGroup.getId());
            savedOrderTable.updateEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.updateOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}

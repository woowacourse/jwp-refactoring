package kitchenpos.application;

import kitchenpos.application.dto.OrderTableIdRequestDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.TableGroupCreateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public TableGroup create(final TableGroupCreateRequestDto dto) {
        final List<OrderTableIdRequestDto> orderTables = dto.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(getRequestOrderTablesIds(orderTables));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        tableGroup.validateIsEqualToOrderTablesSize(orderTables.size());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        changeOrderTableFull(savedOrderTables, tableGroup);
        return savedTableGroup;
    }

    private void changeOrderTableFull(List<OrderTable> savedOrderTables, TableGroup tableGroup) {
        final Long tableGroupId = tableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.doTabling(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        tableGroup.setOrderTables(savedOrderTables);
    }

    private List<Long> getRequestOrderTablesIds(List<OrderTableIdRequestDto> orderTables) {
        return orderTables.stream()
                .map(OrderTableIdRequestDto::getId)
                .collect(Collectors.toList());
    }

    @Transactional
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
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}

package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroup create(final TableGroup tableGroup) {
        tableGroup.validateSizeOfOrderTables();
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        tableGroup.validateExistOrderTable(savedOrderTables.size());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateGroupable();
        }

        final TableGroup newTableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(newTableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        final List<OrderTable> newOrderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            final OrderTable newOrderTable = new OrderTable(savedOrderTable.getId(), tableGroupId,
                    savedOrderTable.getNumberOfGuests(), false);
            newOrderTables.add(orderTableDao.save(newOrderTable));
        }

        return new TableGroup(tableGroupId, savedTableGroup.getCreatedDate(), newOrderTables);
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
            final OrderTable newOrderTable = new OrderTable(orderTable.getId(), null,
                    orderTable.getNumberOfGuests(), false);
            orderTableDao.save(newOrderTable);
        }
    }
}

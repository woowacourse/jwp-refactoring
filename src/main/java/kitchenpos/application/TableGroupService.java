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
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<Table> tables = tableGroup.getTables();

        if (CollectionUtils.isEmpty(tables) || tables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = tables.stream()
                .map(Table::getId)
                .collect(Collectors.toList());

        final List<Table> savedTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (tables.size() != savedTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final Table savedTable : savedTables) {
            if (!savedTable.isEmpty() || Objects.nonNull(savedTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final Table savedTable : savedTables) {
            savedTable.putInGroup(tableGroupId);
            orderTableDao.save(savedTable);
        }
        savedTableGroup.setTables(savedTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = tables.stream()
                .map(Table::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        for (final Table table : tables) {
            table.excludeFromGroup();
            orderTableDao.save(table);
        }
    }
}

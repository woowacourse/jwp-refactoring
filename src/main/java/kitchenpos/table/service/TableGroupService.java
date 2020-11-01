package kitchenpos.table.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.TableGroupCreateRequest;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final TableDao tableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final TableDao tableDao,
        final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public Long create(TableGroupCreateRequest request) {
        Set<Long> tableIds = request.getTableIds();

        if (CollectionUtils.isEmpty(tableIds) || tableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Table> savedTables = tableDao.findAllByIdIn(new ArrayList<>(tableIds));

        TableGroup tableGroup = new TableGroup(savedTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final Table savedTable : savedTables) {
            savedTable.changeTableGroupId(tableGroupId);
            savedTable.fill();
            tableDao.save(savedTable);
        }

        savedTableGroup.changeOrderTables(savedTables);

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> tableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            tableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.changeTableGroupId(null);
            table.fill();
            tableDao.save(table);
        }
    }
}

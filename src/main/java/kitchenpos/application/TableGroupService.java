package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final TableDao tableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final TableDao tableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> tableIds = tableGroupCreateRequest.getTableIds();

        final List<Table> savedTables = tableDao.findAllByIdIn(tableIds);

        if (tableIds.size() != savedTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final Table savedTable : savedTables) {
            if (!savedTable.isEmpty() || Objects.nonNull(savedTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final Table savedTable : savedTables) {
            savedTable.joinIn(tableGroupId);
            tableDao.save(savedTable);
        }

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> tableIds = tables.stream()
                .map(Table::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByTableIdInAndOrderStatusIn(
                tableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.ungroup();
            tableDao.save(table);
        }
    }
}

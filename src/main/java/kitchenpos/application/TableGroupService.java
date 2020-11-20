package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final int MIN_TABLE_NUMBER = 2;

    private final OrderDao orderDao;
    private final TableDao tableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final TableDao tableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Table> savedTables = findValidSavedTables(tableGroupCreateRequest);

        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());

        Long tableGroupId = savedTableGroup.getId();
        for (Table savedTable : savedTables) {
            savedTable.joinIn(tableGroupId);
            tableDao.save(savedTable);
        }

        return savedTableGroup;
    }

    private List<Table> findValidSavedTables(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> tableIds = tableGroupCreateRequest.getTableIds();

        validateSize(tableIds);
        return validateTables(tableIds);
    }

    private void validateSize(List<Long> tableIds) {
        if (tableIds.size() < MIN_TABLE_NUMBER) {
            throw new NotEnoughTableException();
        }

        for (Long tableId : tableIds) {
            if (Objects.isNull(tableId)) {
                throw new NullRequestException();
            }
        }
    }

    private List<Table> validateTables(List<Long> tableIds) {
        List<Table> savedTables = tableDao.findAllByIdIn(tableIds);

        if (tableIds.size() != savedTables.size()) {
            throw new TableNotExistenceException();
        }

        for (Table savedTable : savedTables) {
            if (!savedTable.isEmpty()) {
                throw new TableNotEmptyException();
            }

            if (savedTable.hasGroup()) {
                throw new TableGroupExistenceException();
            }
        }

        return savedTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<Table> tables = tableDao.findAllByTableGroupId(tableGroupId);

        validateOrderStatus(tables);
        for (final Table table : tables) {
            table.ungroup();
            tableDao.save(table);
        }
    }

    private void validateOrderStatus(List<Table> tables) {
        List<Long> tableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());

        List<Order> foundOrders = orderDao.findByTableIds(tableIds);

        for (Order foundOrder : foundOrders) {
            if (foundOrder.hasInProgressStatus()) {
                throw new TableGroupCannotChangeException();
            }
        }
    }
}

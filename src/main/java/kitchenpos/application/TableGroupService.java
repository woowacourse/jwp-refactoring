package kitchenpos.application;

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
import kitchenpos.exception.NotEnoughTableException;
import kitchenpos.exception.NullRequestException;
import kitchenpos.exception.TableGroupExistenceException;
import kitchenpos.exception.TableNotEmptyException;
import kitchenpos.exception.TableNotExistenceException;

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

            if (Objects.nonNull(savedTable.getTableGroupId())) {
                throw new TableGroupExistenceException();
            }
        }

        return savedTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> tableIds = tables.stream()
                .map(Table::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByTableIdInAndOrderStatusIn(
                tableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.ungroup();
            tableDao.save(table);
        }
    }
}

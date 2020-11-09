package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> tableIds = Optional.ofNullable(request.getTableIds())
            .orElseThrow(IllegalArgumentException::new);
        final List<Table> savedTables = tableDao.findAllByIdIn(tableIds);

        validSameSize(tableIds, savedTables);
        validGroupable(savedTables);
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), savedTables));

        final Long tableGroupId = savedTableGroup.getId();
        for (final Table savedTable : savedTables) {
            savedTable.putInGroup(tableGroupId);
            tableDao.save(savedTable);
        }
        return TableGroupResponse.of(savedTableGroup, savedTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());

        if (isNotMealOver(orderTableIds)) {
            throw new IllegalArgumentException();
        }
        for (final Table table : tables) {
            table.excludeFromGroup();
            tableDao.save(table);
        }
    }

    private boolean isNotMealOver(List<Long> orderTableIds) {
        return orderDao.existsByTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

    private void validGroupable(List<Table> savedTables) {
        for (final Table savedTable : savedTables) {
            if (!savedTable.isEmpty() || savedTable.isGrouped()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void validSameSize(List<Long> tableIds, List<Table> savedTables) {
        if (tableIds.size() != savedTables.size()) {
            throw new IllegalArgumentException();
        }
    }
}

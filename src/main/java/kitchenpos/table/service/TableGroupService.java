package kitchenpos.table.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableGroupCreateRequest;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderDao orderDao;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, TableRepository tableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public Long create(TableGroupCreateRequest request) {
        final List<Table> savedTables = tableRepository.findAllByIdIn(new ArrayList<>(request.getTableIds()));

        TableGroup tableGroup = new TableGroup(savedTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // TODO 이거 JPA 옵션 있을텐데
        for (final Table savedTable : savedTables) {
            savedTable.changeTableGroup(savedTableGroup);
            savedTable.fill();
        }

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> tableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            tableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.changeTableGroup(null);
            table.fill();
            tableRepository.save(table);
        }
    }
}

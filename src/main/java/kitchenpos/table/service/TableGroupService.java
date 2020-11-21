package kitchenpos.table.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.TableRepository;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.Tables;
import kitchenpos.table.dto.TableGroupCreateRequest;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, TableRepository tableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public Long create(TableGroupCreateRequest request) {
        final List<Table> savedTables = tableRepository.findAllByIdIn(
            new ArrayList<>(request.getTableIds()));

        TableGroup tableGroup = new TableGroup(savedTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final Table savedTable : tableGroup.getTables()) {
            savedTable.changeTableGroup(savedTableGroup);
        }

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        Tables tables = new Tables(tableRepository.findAllByTableGroupId(tableGroupId));
        tables.unGroup();
    }
}

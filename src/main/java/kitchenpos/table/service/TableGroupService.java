package kitchenpos.table.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupCreateRequest;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public Long create(TableGroupCreateRequest request) {
        final List<Table> savedTables = orderTableRepository.findAllByIdIn(
            new ArrayList<>(request.getTableIds()));

        TableGroup tableGroup = new TableGroup(savedTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // TODO 이거 JPA 옵션 있을텐데
        for (final Table savedTable : tableGroup.getTables()) {
            savedTable.changeTableGroup(savedTableGroup);
            savedTable.fill();
        }

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> tableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());
        //
        // List<Order> orders = orderRepository.findAllByTableIdIn(tableIds);
        //
        // orders
        //
        if (orderRepository.existsByTableIdInAndOrderStatusIn(
            tableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.changeTableGroup(null);
            table.fill();
            orderTableRepository.save(table);
        }
    }
}

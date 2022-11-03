package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final TableGroup tableGroup = tableGroupCreateRequest.toTableGroup();
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroup);
        tableGroup.validateExistOrderTable(savedOrderTables.size());

        final TableGroup newTableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(newTableGroup);

        final List<OrderTable> groupedOrderTables = groupOrderTable(savedOrderTables, savedTableGroup.getId());

        savedTableGroup.updateOrderTables(groupedOrderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private List<OrderTable> groupOrderTable(final List<OrderTable> savedOrderTables, final Long tableGroupId) {
        final List<OrderTable> newOrderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupTableBy(tableGroupId);
            newOrderTables.add(orderTableRepository.save(savedOrderTable));
        }
        return newOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOrderTableNotInCompletion(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateOrderTableNotInCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderTableValidator.validate(orderTableIds);
    }
}

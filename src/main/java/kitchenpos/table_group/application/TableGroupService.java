package kitchenpos.table_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order_table.repository.OrderTableRepository;
import kitchenpos.table_group.repository.TableGroupRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.order_table.dto.OrderTableIdRequest;
import kitchenpos.table_group.dto.TableGroupRequest;
import kitchenpos.table_group.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
            .stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        validateOrderTablesCount(orderTableIds);

        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        validateSavedTable(orderTableIds, savedOrderTables);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(savedOrderTables)
            .build();

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateOrderTablesCount(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSavedTable(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        validateOrderStatus(tableGroup.getOrderTables());

        tableGroup.ungroup();

        tableGroupRepository.save(tableGroup);
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        if (isAnyOrderTableInProgress(orderTables)){
            throw new IllegalArgumentException();
        }
    }

    private boolean isAnyOrderTableInProgress(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .anyMatch(OrderTable::isInProgress);
    }
}

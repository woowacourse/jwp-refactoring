package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.GroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupingRequest;
import kitchenpos.application.dto.result.TableGroupResult;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.dao.table.TableGroupRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.OrderTablesValidator;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTablesValidator orderTablesValidator;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidationService tableValidationService;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final OrderTablesValidator orderTablesValidator,
            final TableGroupRepository tableGroupRepository,
            final TableValidationService tableValidationService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTablesValidator = orderTablesValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidationService = tableValidationService;
    }

    @Transactional
    public TableGroupResult create(final TableGroupingRequest request) {
        final OrderTables orderTables = getOrderTablesByRequest(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        orderTables.groupByTableGroup(tableGroup, orderTablesValidator);
        orderTableRepository.saveAll(orderTables.getOrderTables());
        return TableGroupResult.from(tableGroupRepository.save(tableGroup));
    }

    private OrderTables getOrderTablesByRequest(final TableGroupingRequest request) {
        final List<Long> orderTableIds = extractOrderTableIds(request);
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateTablesExist(orderTableIds, orderTables);
        return new OrderTables(orderTables);
    }

    private List<Long> extractOrderTableIds(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void validateTablesExist(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("Order table does not exist.");
        }
    }

    @Transactional
    public void ungroup(final Long ungroupTableId) {
        final TableGroup tableGroup = tableGroupRepository.findById(ungroupTableId)
                .orElseThrow(() -> new IllegalArgumentException("Table group does not exist."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validateTableGroupIsAbleToUngroup(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateTableGroupIsAbleToUngroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            tableValidationService.validateUngroupAvailable(orderTable.getId());
        }
    }
}

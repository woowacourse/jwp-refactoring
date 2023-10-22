package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupingService tableGroupingService;
    private final OrderTablesValidator orderTablesValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final TableGroupingService tableGroupingService,
            final OrderTablesValidator orderTablesValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupingService = tableGroupingService;
        this.orderTablesValidator = orderTablesValidator;
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
        final Map<Long, OrderTable> orderTablesById = orderTableRepository.findAllByIdIn(orderTableIds).stream()
                .collect(Collectors.toMap(OrderTable::getId, Function.identity()));
        return new OrderTables(request.getOrderTables().stream().map(orderTableRequest ->
                getOrderTableByRequest(orderTableRequest, orderTablesById)
        ).collect(Collectors.toList()));
    }

    private List<Long> extractOrderTableIds(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private OrderTable getOrderTableByRequest(
            final GroupOrderTableRequest orderTableRequest,
            final Map<Long, OrderTable> orderTablesById
    ) {
        return orderTablesById.computeIfAbsent(orderTableRequest.getId(), id -> {
            throw new IllegalArgumentException("Order table does not exist.");
        });
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
            tableGroupingService.isAbleToUngroup(orderTable.getId());
        }
    }
}

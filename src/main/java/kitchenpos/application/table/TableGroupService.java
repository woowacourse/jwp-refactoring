package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.GroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupingRequest;
import kitchenpos.application.dto.result.TableGroupResult;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.dao.table.TableGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResult create(final TableGroupingRequest request) {
        final List<OrderTable> orderTables = getOrderTablesByRequest(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        groupingOrderTables(orderTables, tableGroup);
        orderTableRepository.saveAll(orderTables);
        return TableGroupResult.from(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTablesByRequest(final TableGroupingRequest request) {
        final List<Long> orderTableIds = extractOrderTableIds(request);
        final Map<Long, OrderTable> orderTablesById = orderTableRepository.findAllByIdIn(orderTableIds).stream()
                .collect(Collectors.toMap(OrderTable::getId, Function.identity()));
        return request.getOrderTables().stream().map(orderTableRequest ->
                getOrderTableByRequest(orderTableRequest, orderTablesById)).collect(Collectors.toList());
    }

    private OrderTable getOrderTableByRequest(
            final GroupOrderTableRequest orderTableRequest,
            final Map<Long, OrderTable> orderTablesById
    ) {
        return orderTablesById.computeIfAbsent(orderTableRequest.getId(), id -> {
            throw new IllegalArgumentException("Order table does not exist.");
        });
    }

    private List<Long> extractOrderTableIds(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private void groupingOrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        validateSize(orderTables);
        validateGroupOrderTableIsAvailable(orderTables);
        orderTables.forEach(orderTable -> orderTable.groupByTableGroup(tableGroup));
    }

    private void validateGroupOrderTableIsAvailable(final List<OrderTable> orderTables) {
        if (!isOrderTablesAbleToGroup(orderTables)) {
            throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
        }
    }

    private boolean isOrderTablesAbleToGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isAbleToGroup);
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("Table group must have at least two tables.");
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
            validateTableIsAbleToUngroup(orderTable);
        }
    }

    private void validateTableIsAbleToUngroup(final OrderTable orderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("Cannot ungroup non-completed table.");
        }
    }
}

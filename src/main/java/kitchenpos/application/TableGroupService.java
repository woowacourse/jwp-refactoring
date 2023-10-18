package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.GroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupingRequest;
import kitchenpos.application.dto.result.TableGroupResult;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResult create(final TableGroupingRequest request) {
        final List<OrderTable> orderTables = getOrderTablesByRequest(request);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
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

    @Transactional
    public void ungroup(final Long ungroupTableId) {
        final TableGroup tableGroup = tableGroupRepository.findById(ungroupTableId)
                .orElseThrow(() -> new IllegalArgumentException("Table group does not exist."));
        tableGroup.ungroupOrderTables();
    }
}

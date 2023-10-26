package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.GroupOrderTableRequest;
import kitchenpos.table.application.dto.TableGroupingRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderTablesMapper {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public OrderTablesMapper(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public OrderTables from(final TableGroupingRequest request) {
        return new OrderTables(getOrderTablesByRequest(request));
    }

    private List<OrderTable> getOrderTablesByRequest(final TableGroupingRequest request) {
        final List<Long> orderTableIds = extractOrderTableIds(request);
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateTablesExist(orderTableIds, orderTables);
        return orderTables;
    }

    private List<Long> extractOrderTableIds(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateTablesExist(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("Order table does not exist.");
        }
    }

    public OrderTables fromTable(final Long tableId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table group does not exist."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        return new OrderTables(orderTables);
    }
}

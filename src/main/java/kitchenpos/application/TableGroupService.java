package kitchenpos.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTablesToCreateGroup;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.InvalidStateException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTablesToCreateGroup orderTables = convertToOrderTables(tableGroupRequest.getOrderTables());

        final TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        orderTables.changeAllEmptyToFalse();
        orderTables.assign(tableGroup);

        return convertToTableGroupResponse(tableGroup, orderTables.getOrderTables());
    }

    private TableGroupResponse convertToTableGroupResponse(TableGroup tableGroup, List<OrderTable> orderTables) {
        final List<OrderTableResponse> orderTableResponses = convertToOrderTableResponses(orderTables);
        return new TableGroupResponse(tableGroup, orderTableResponses);
    }

    private List<OrderTableResponse> convertToOrderTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toList())
            ;
    }

    private OrderTablesToCreateGroup convertToOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> requestOrderTablesIds = extractOrderTableRequestsIds(orderTableRequests);
        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByIdIn(requestOrderTablesIds);
        validateAllOrderTablesExistsInDB(foundOrderTables, requestOrderTablesIds);

        return new OrderTablesToCreateGroup(foundOrderTables);
    }

    private List<Long> extractOrderTableRequestsIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList())
            ;
    }

    private void validateAllOrderTablesExistsInDB(List<OrderTable> foundOrderTables, List<Long> requestOrderTablesIds) {
        final Set<Long> orderTableIds = extractOrderTableIds(foundOrderTables);
        final Set<Long> requestOrderTableIdsDuplicateRemoved = new HashSet<>(requestOrderTablesIds);
        if (!orderTableIds.containsAll(requestOrderTableIdsDuplicateRemoved)) {
            throw new NotFoundException("요청 OrderTable Id들 중, DB에 존재하지 않는것이 있습니다.");
        }
    }

    private Set<Long> extractOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toSet())
            ;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateAllOrdersCompleted(orderTables);
        ungroupAllOrderTables(orderTables);
    }

    private void validateAllOrdersCompleted(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, OrderStatus.getExceptCompletion())) {
            throw new InvalidStateException("COMPLETION 상태가 아닌 Order가 존재합니다.");
        }
    }

    private void ungroupAllOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}

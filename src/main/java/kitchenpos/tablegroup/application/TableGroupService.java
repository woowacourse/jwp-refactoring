package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.dto.request.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = getOrderTableIds(request.getOrderTables());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        orderTables.forEach(OrderTable::validateCreateTableGroup);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(savedTableGroup);
            orderTable.updateEmpty(false);
        }
        return convertToResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validateOrderStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(null);
            orderTable.updateEmpty(true);
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTableIdRequest> orderTableIdRequests) {
        final List<Long> orderTableIds = orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        validateOrderTableIdsSize(orderTableIds);
        return orderTableIds;
    }

    private void validateOrderTableIdsSize(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private TableGroupResponse convertToResponse(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableRepository.findAllByTableGroup(tableGroup).stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList())
        );
    }

    private OrderTableResponse convertToResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroup().getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}

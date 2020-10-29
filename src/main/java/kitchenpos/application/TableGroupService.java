package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTables;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.dto.order.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = getOrderTablesFromRequest(tableGroupRequest);

        final List<Long> orderTableIds = orderTables.getOrderTableIds();

        final OrderTables savedOrderTables = OrderTables.of(orderTableRepository.findAllByIdIn(orderTableIds));

        orderTables.isInvalidOrderTables(savedOrderTables);

        savedOrderTables.validateGroupTables();

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        final List<OrderTable> groupOrderTables = saveOrderTableWithGroup(orderTableIds, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, groupOrderTables);
    }

    private OrderTables getOrderTablesFromRequest(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        final List<OrderTable> requestOrderTables = orderTableRequests.stream()
                .map(OrderTableRequest::forOrderTables)
                .collect(Collectors.toList());
        return OrderTables.of(requestOrderTables);
    }

    private List<OrderTable> saveOrderTableWithGroup(final List<Long> orderTableIds, final TableGroup savedTableGroup) {
        final List<OrderTable> groupOrderTables = new ArrayList<>();
        for (final Long orderTableId : orderTableIds) {
            OrderTable orderTable = orderTableRepository.findById(orderTableId)
                    .orElseThrow(IllegalArgumentException::new);
            orderTable.groupIn(savedTableGroup);
            groupOrderTables.add(orderTableRepository.save(orderTable));
        }
        return groupOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateContainOrder(orderTables);

        ungroupOrderTables(orderTables);
    }

    private void validateContainOrder(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void ungroupOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }
}

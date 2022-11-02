package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exceptions.OrderNotCompletionException;
import kitchenpos.exceptions.OrderTableAlreadyHasTableGroupException;
import kitchenpos.exceptions.OrderTableNotEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findAvailableOrderTables(tableGroupRequest);
        final TableGroup tableGroup = TableGroupRequest.from(orderTables);
        tableGroupRepository.save(tableGroup);
        tableGroup.setOrderTablesEmpty();
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> findAvailableOrderTables(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        validateOrderTables(orderTables);
        return orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new OrderTableNotEmptyException();
            }
            if (orderTable.hasTableGroup()) {
                throw new OrderTableAlreadyHasTableGroupException();
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatusCompletion(orderTables);
        ungroupOrderTables(orderTables);
    }

    private void validateOrderStatusCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(COOKING, MEAL))) {
            throw new OrderNotCompletionException();
        }
    }

    private static void ungroupOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}

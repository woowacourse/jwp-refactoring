package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.order.application.dto.OrderTablesRequest;
import kitchenpos.order.application.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.order.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.order.domain.exception.TableGroupException.ExistsNotCompletionOrderException;
import kitchenpos.order.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private static final int LOWER_BOUND_ORDER_TABLE_SIZE = 2;

    private static final List<OrderStatus> CANNOT_UNGROUP_ORDER_STATUSES = List.of(OrderStatus.COOKING,
            OrderStatus.MEAL);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final OrderTablesRequest orderTablesRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTablesRequest.getOrderTableIds());
        validateTableGroup(orderTables, orderTablesRequest.getOrderTableIds());
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        for (OrderTable orderTable : orderTables) {
            orderTable.group(savedTableGroup);
        }
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    private void validateTableGroup(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new NotExistsOrderTableException();
        }
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < LOWER_BOUND_ORDER_TABLE_SIZE) {
            throw new InsufficientOrderTableSizeException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new CannotAssignOrderTableException();
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);

        validateUngroup(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }

        tableGroupRepository.deleteById(tableGroupId);
    }

    private void validateUngroup(final List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, CANNOT_UNGROUP_ORDER_STATUSES)) {
            throw new ExistsNotCompletionOrderException();
        }
    }
}

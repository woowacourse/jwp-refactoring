package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.validateGroupingTable();
        orderTables.validateOrderTableCount(orderTableIds);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toEntity());

        orderTables.updateTableGroup(savedTableGroup);

        return TableGroupResponse.of(
                savedTableGroup,
                OrderTableResponse.ofList(
                        orderTables.getOrderTables()
                )
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        final OrderTables orderTables = new OrderTables(savedOrderTables);

        final List<Long> orderTableIds = orderTables.getOrderTableIds();

        validateOrdersStatusCompletion(orderTableIds);

        orderTables.updateTableGroup(null);
    }

    private void validateOrdersStatusCompletion(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}

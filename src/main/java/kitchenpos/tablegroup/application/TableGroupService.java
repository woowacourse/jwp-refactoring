package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.request.OrderTableGroupCreateRequest;
import kitchenpos.tablegroup.application.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroup create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        final List<OrderTable> orderTablesWithTableGroup = addTableGroup(request, tableGroup);
        validateSize(orderTablesWithTableGroup);
        validateDuplicate(request, orderTablesWithTableGroup);

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderStatus(orderTableIds);

        ungroupOrderTables(orderTables);
    }

    private OrderTable toOrderTable(final long orderId, final TableGroup tableGroup) {
        final OrderTable orderTable = orderTableRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 OrderTable이 존재하지 않습니다."));
        orderTable.addTableGroup(tableGroup.getId());
        return orderTable;
    }

    private List<OrderTable> addTableGroup(final TableGroupCreateRequest request, final TableGroup tableGroup) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableGroupCreateRequest::getId)
                .distinct()
                .map(it -> toOrderTable(it, tableGroup))
                .collect(Collectors.toList());
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("OrderTable의 크기가 2 미만입니다.");
        }
    }

    private void validateDuplicate(final TableGroupCreateRequest request, final List<OrderTable> orderTables) {
        if (request.getOrderTables().size() != orderTables.size()) {
            throw new IllegalArgumentException("입력받은 OrderTable 중 존재하지 않는 것이 있습니다.");
        }
    }

    private static List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("해당 TableGroup의 Order 중 완료되지 않은 것이 존재합니다.");
        }
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroup();
        }
    }
}

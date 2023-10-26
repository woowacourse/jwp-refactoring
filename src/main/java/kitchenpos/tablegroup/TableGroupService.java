package kitchenpos.tablegroup;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        List<OrderTable> orderTables = toOrderTables(tableGroup.getOrderTables());
        validateSizeOf(orderTables);
        TableGroup newTableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.forEach(table -> table.assign(newTableGroup.getId()));

        return TableGroupResponse.from(newTableGroup, orderTables);
    }

    private void validateSizeOf(List<OrderTable> orderTables) {
        /*private static */
        final int MINIMUM_GROUP_SIZE = 2;
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(MINIMUM_GROUP_SIZE + "개 이상의 테이블이 필요합니다");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("그런 테이블 그룹은 없습니다"));
        //
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            validateNoOngoing(orders);
            orderTable.ungroup();
        }
        //
    }

    //
    private void validateNoOngoing(List<Order> orders) {
        if (hasAnyOngoing(orders)) {
            throw new IllegalArgumentException("이미 진행중인 주문이 있습니다");
        }
    }

    private boolean hasAnyOngoing(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::isOngoing);
    }
    //

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }
}

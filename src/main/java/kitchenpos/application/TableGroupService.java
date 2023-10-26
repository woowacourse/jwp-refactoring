package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroup create(final TableGroupRequest tableGroup) {
        TableGroup newTableGroup = new TableGroup(toOrderTables(tableGroup.getOrderTables()));

        return tableGroupRepository.save(newTableGroup);
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

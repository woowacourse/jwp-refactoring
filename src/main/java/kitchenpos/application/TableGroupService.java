package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("유효하지 않은 OrderTable을 포함하고 있습니다.");
        }

        final TableGroup tableGroup = new TableGroup(orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByGroupId(tableGroupId);

        final List<Order> orders = orderRepository.findByOrderByTableGroupId(tableGroupId);
        final boolean containsNotCompletionOrder = orders.stream()
                .anyMatch(Order::isNotCompletionStatus);
        if (containsNotCompletionOrder) {
            throw new IllegalArgumentException("이미 주문이 진행 중이에요");
        }


        orderTables.forEach(OrderTable::ungroup);
    }
}

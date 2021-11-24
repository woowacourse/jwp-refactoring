package kitchenpos.tablegroup.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.tablegroup.service.dto.TableGroupRequest;
import kitchenpos.tablegroup.service.dto.TableGroupResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {
    private static final int MIN_GROUP_SIZE = 2;

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

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateCreation(savedOrderTables, orderTableIds);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedOrderTables.forEach(it -> it.belongsTo(savedTableGroup.getId()));

        return TableGroupResponse.of(savedTableGroup);
        // orderTable 그룹 도메인 이벤트
    }

    private void validateCreation(List<OrderTable> orderTables, List<Long> requestedIds) {
        if (requestedIds.size() < MIN_GROUP_SIZE) {
            throw new IllegalStateException("단체 지정할 테이블들이 부족합니다.");
        }

        if (requestedIds.size() != orderTables.size()) {
            throw new NoSuchElementException();
        }
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(
            orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList())
        );
        orders.forEach(Order::validateCompleted);

        orderTables.forEach(OrderTable::ungroup);
    }
}

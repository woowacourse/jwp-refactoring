package kitchenpos.tablegroup;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupManager {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public GroupManager(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void group(List<Long> orderTableIds, Long tableGroupId) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateGroup(orderTableIds, savedOrderTables);

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroupId);
        }
    }

    private void validateGroup(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("그룹하려는 테이블과 저장된 테이블의 수가 같아야 합니다.");
        }
        if (savedOrderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
        if (savedOrderTables.stream().anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 그룹으로 지정할 수 없습니다.");
        }
        if (savedOrderTables.stream().anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException("이미 그룹으로 지정된 테이블은 그룹으로 지정할 수 없습니다.");
        }
    }

    public void unGroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        validateUnGroup(orderTables, orders);

        for (OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }

    private void validateUnGroup(List<OrderTable> orderTables, List<Order> orders) {
        if (orders.stream()
                .anyMatch(Order::isCookingOrMeal)) {
            throw new IllegalArgumentException("조리중 또는 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
        if (orderTables.stream()
                .anyMatch(OrderTable::hasNoTableGroup)) {
            throw new IllegalArgumentException("그룹으로 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
        }
    }
}

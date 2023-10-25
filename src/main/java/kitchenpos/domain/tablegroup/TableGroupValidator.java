package kitchenpos.domain.tablegroup;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.dto.request.CreateTableGroupRequest.CreateOrderTable;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreate(CreateTableGroupRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderTables()) || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("단체 지정은 2개 이상의 테이블이 필요합니다.");
        }

        List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(CreateOrderTable::getId)
                .map(tableId -> orderTableRepository.findById(tableId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다.")))
                .collect(Collectors.toList());

        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void validateUngroup(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTableRepository.findAllByTableGroupId(tableGroup.getId())) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTable.getId());
        if (order.isEmpty()) {
            return;
        }
        if (order.get().getOrderStatus() == OrderStatus.COOKING || order.get().getOrderStatus() == OrderStatus.MEAL) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 테이블은 단체지정을 해제할 수 없습니다.");
        }
    }
}

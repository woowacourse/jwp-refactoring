package kitchenpos.table;

import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableValidator {

    private OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateGroupOrderTableExist(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 지정했습니다. 단체 지정 할 수 없습니다.");
        }
    }

    public void validateUngroupTableOrderCondition(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("테이블의 주문이 이미 조리 혹은 식사 중입니다. 단체 지정을 삭제할 수 없습니다.");
        }

    }
}

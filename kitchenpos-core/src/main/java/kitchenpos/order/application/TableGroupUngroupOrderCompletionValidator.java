package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.OrderCompletionValidator;
import kitchenpos.tablegroup.exception.InvalidTableGroupUngroupException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;

@Component
public class TableGroupUngroupOrderCompletionValidator implements OrderCompletionValidator {
    
    private final OrderRepository orderRepository;
    
    public TableGroupUngroupOrderCompletionValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    @Override
    public void validateIfOrderOfOrderTableIsCompleted(final List<OrderTable> orderTables, final Long tableGroupId) {
        final List<List<Order>> ordersInEachTable = orderTables.stream()
                                                               .map(orderTable -> orderRepository.findAllByOrderTableId(orderTable.getId()))
                                                               .collect(Collectors.toList());
        for (List<Order> orders : ordersInEachTable) {
            validateIfOrdersOfTableCompleted(orders);
        }
    }
    
    private void validateIfOrdersOfTableCompleted(final List<Order> orders) {
        boolean isOrderOfTableInProgress = orders.stream()
                                                 .anyMatch(order -> order.getOrderStatus() != COMPLETION);
        if (isOrderOfTableInProgress) {
            throw new InvalidTableGroupUngroupException("단체 테이블에 속하는 테이블의 주문 상태가 COOKING 혹은 MEAL이면 단체 테이블을 해제할 수 없습니다");
        }
    }
}

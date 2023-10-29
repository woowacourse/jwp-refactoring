package kitchenpos.table;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableValidator {

    private OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//    public void validateChangeEmptyTableOrderCondition(final Long orderTableId) {
//        final List<Order> tableOrders = orderRepository.findByOrderTableId(orderTableId);
//
//        for (Order order : tableOrders) {
//            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
//                throw new IllegalArgumentException("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");
//            }
//        }
//    }

    public void validateGroupOrderTableExist(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 지정했습니다. 단체 지정 할 수 없습니다.");
        }
    }

    public void validateUngroupTableOrderCondition(final List<OrderTable> orderTables) {
        final List<Order> orders = orderTables.stream()
                .flatMap(orderTable -> orderRepository.findByOrderTableId(orderTable.getId()).stream())
                .collect(Collectors.toList());

        for (Order order : orders) {
            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
                throw new IllegalArgumentException("테이블의 주문이 이미 조리 혹은 식사 중입니다. 단체 지정을 삭제할 수 없습니다.");
            }
        }
    }
}

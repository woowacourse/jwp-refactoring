package kitchenpos.domain.table;

import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;

import java.util.List;


public abstract class OrderTableValidator {

    private OrderTableValidator() {
    }

    public static void validateChangeEmptyTableOrderCondition(List<Order> tableOrders) {
        for (Order order : tableOrders) {
            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
                throw new IllegalArgumentException("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");
            }
        }
    }
}

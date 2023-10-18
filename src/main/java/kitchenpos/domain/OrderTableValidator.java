package kitchenpos.domain;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

import java.util.List;
import java.util.Objects;


public abstract class OrderTableValidator {

    private OrderTableValidator() {
    }

    public static void validateChangeEmpty(OrderTable orderTable, List<Order> orders) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("테이블이 이미 단체 지정되었습니다. 빈 테이블로 변경할 수 없습니다.");
        }

        for (Order order : orders) {
            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
                throw new IllegalArgumentException("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");
            }
        }
    }
}

package kitchenpos.order.vo;

import java.util.List;
import kitchenpos.order.domain.Order;

public class TableOrders {

    private final List<Order> orders;

    public TableOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void validateHasCookingOrMealOrder() {
        if (hasCookingOrMealOrder()) {
            throw new IllegalArgumentException("조리 혹은 식사 중인 주문이 존재하는 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
    }

    private boolean hasCookingOrMealOrder() {
        return orders.stream().anyMatch(Order::isCookingOrMealStatus);
    }
}

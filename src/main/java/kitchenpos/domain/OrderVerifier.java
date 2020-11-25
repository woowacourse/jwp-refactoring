package kitchenpos.domain;

import java.util.List;

public class OrderVerifier {
    public static void validateOrderStatus(List<Order> orders) {
        for (Order order : orders) {
            if (!order.isComplete()) {
                throw new IllegalArgumentException("테이블의 주문이 아직 결제되지 않았습니다.");
            }
        }
    }
}

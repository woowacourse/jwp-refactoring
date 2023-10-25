package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus parseOrderStatus(String orderStatus) {
        try {
            return OrderStatus.valueOf(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new OrderException("유효하지 않은 주문상태라 주문상태를 변경할 수 없습니다.");
        }
    }
}

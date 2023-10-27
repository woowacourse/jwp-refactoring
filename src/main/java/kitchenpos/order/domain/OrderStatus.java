package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.order.exception.OrderStatusException;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static void checkIfHas(String orderStatus) {
        if (Arrays.stream(OrderStatus.values())
                .noneMatch(status -> status.name().equals(orderStatus))) {
            throw new OrderStatusException("해당하는 주문 상태가 없습니다.");
        }
    }

    public static boolean checkWhetherCompletion(OrderStatus orderStatus) {
        return orderStatus == COMPLETION;
    }

    public static boolean checkWhetherMeal(OrderStatus orderStatus) {
        return orderStatus == MEAL;
    }

    public static boolean checkWhetherCooking(OrderStatus orderStatus) {
        return orderStatus == COOKING;
    }
}

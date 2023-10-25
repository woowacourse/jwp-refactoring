package kitchenpos.domain.order;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompleted(String status) {
        return OrderStatus.valueOf(status) == COMPLETION;
    }
}

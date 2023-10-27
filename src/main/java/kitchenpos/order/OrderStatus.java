package kitchenpos.order;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompleted(String status) {
        return valueOf(status) == COMPLETION;
    }
}

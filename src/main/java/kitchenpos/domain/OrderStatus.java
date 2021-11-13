package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(OrderStatus orderStatus) {
        return orderStatus == COMPLETION;
    }
}

package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(String name) {
        return name.equals(COMPLETION.name());
    }
}

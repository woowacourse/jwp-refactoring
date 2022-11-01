package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(String name) {
        return name.equals(COMPLETION.name());
    }

    public OrderStatus updateTo(final String name) {
        if (isCompletion(this.name())) {
            throw new IllegalArgumentException();
        }
        return OrderStatus.valueOf(name);
    }
}

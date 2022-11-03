package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public OrderStatus updateTo(final String name) {
        if (isCompletion()) {
            throw new IllegalArgumentException();
        }
        return OrderStatus.valueOf(name);
    }

    public boolean isCompletion() {
        return this.equals(COMPLETION);
    }
}

package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCooking() {
        return this.equals(COOKING);
    }

    public boolean isMeal() {
        return this.equals(MEAL);
    }
}

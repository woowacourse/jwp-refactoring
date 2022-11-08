package kitchenpos.product.domain;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public boolean isStatus(final String status) {
        return this.name().equals(status);
    }
}

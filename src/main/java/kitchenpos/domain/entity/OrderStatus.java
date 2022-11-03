package kitchenpos.domain.entity;

public enum OrderStatus {
    COOKING("COOKING"), MEAL("MEAL"), COMPLETION("COMPLETION");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

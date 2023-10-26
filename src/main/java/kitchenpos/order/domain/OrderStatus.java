package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus resolve(String orderStatus) {
        switch (orderStatus) {
            case "COOKING":
                return COOKING;
            case "MEAL":
                return MEAL;
            case "COMPLETION":
                return COMPLETION;
            default:
                return null;
        }
    }
}

package kitchenpos.domain.table;

public enum TableStatus {
    READY,
    USING
    ;

    public static TableStatus find(final String orderStatus) {
        if (orderStatus.equals("COMPLETION")) {
            return READY;
        }
        return USING;
    }
}

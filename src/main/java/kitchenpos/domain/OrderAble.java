package kitchenpos.domain;

public enum OrderAble {
    POSSIBLE(false), IMPOSSIBLE(true);

    private final boolean isEmptyTable;

    OrderAble(boolean isEmptyTable) {
        this.isEmptyTable = isEmptyTable;
    }

    public static OrderAble of(boolean isEmptyTable) {
        if (isEmptyTable) {
            return IMPOSSIBLE;
        }
        return POSSIBLE;
    }

    public boolean isEmptyTable() {
        return isEmptyTable;
    }
}

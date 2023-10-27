package kitchenpos.table.dto;

public class OrderTableRequest {

    private final Integer numberOfGuest;
    private final Boolean empty;

    public OrderTableRequest(final Integer numberOfGuest, final Boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public Integer getNumberOfGuest() {
        return numberOfGuest;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

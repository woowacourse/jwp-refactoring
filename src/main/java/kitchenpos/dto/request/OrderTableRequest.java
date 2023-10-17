package kitchenpos.dto.request;

public class OrderTableRequest {
    private Integer numberOfGuest;
    private Boolean empty;

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

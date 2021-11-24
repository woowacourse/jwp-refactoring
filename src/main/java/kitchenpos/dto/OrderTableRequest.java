package kitchenpos.dto;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests) {
        this(numberOfGuests, null);
    }

    public OrderTableRequest(Boolean empty) {
        this(null, empty);
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}

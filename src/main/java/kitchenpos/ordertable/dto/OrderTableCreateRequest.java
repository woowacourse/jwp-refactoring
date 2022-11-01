package kitchenpos.ordertable.dto;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Integer numberOfGuests, Boolean empty) {
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

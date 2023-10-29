package kitchenpos.order.application.dto;

public class OrderTableCreateRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

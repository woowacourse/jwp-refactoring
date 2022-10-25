package kitchenpos.dto;

public class OrderTableUpdateRequest {

    private Boolean empty;
    private Integer numberOfGuests;

    public OrderTableUpdateRequest() {
    }

    public OrderTableUpdateRequest(Boolean empty, Integer numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}

package kitchenpos.dto;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableCreateRequest() {
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

package kitchenpos.table.dto;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, Boolean empty) {
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

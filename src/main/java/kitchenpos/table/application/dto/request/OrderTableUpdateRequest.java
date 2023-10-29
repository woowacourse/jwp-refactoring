package kitchenpos.table.application.dto.request;

public class OrderTableUpdateRequest {

    final Integer numberOfGuests;
    final Boolean empty;

    public OrderTableUpdateRequest(final Integer numberOfGuests, final Boolean empty) {
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

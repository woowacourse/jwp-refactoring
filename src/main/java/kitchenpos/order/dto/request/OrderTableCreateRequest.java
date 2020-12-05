package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableCreateRequest {
    @NotNull
    private Integer numberOfGuests;
    @NotNull
    private Boolean empty;

    public OrderTableCreateRequest() {
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

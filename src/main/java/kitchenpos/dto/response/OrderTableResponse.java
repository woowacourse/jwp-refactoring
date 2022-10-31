package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getGuestNumber();
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}

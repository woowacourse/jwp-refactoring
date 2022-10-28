package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableDto {

    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public TableDto() {
    }

    public TableDto(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableDto(OrderTable orderTable) {
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

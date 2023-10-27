package com.kitchenpos.ui.dto;

import com.kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Integer numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    private OrderTableResponse(final Long id,
                               final Integer numberOfGuests,
                               final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

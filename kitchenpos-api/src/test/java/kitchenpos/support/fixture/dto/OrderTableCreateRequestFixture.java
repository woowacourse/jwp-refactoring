package kitchenpos.support.fixture.dto;

import kitchenpos.application.ordertable.dto.OrderTableCreateRequest;

public abstract class OrderTableCreateRequestFixture {

    public static OrderTableCreateRequest orderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }
}

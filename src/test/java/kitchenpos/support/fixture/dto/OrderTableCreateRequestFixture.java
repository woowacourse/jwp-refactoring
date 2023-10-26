package kitchenpos.support.fixture.dto;

import kitchenpos.application.dto.ordertable.OrderTableCreateRequest;

public abstract class OrderTableCreateRequestFixture {

    public static OrderTableCreateRequest orderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }
}

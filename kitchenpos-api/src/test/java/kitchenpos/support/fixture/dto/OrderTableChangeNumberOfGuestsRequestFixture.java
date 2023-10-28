package kitchenpos.support.fixture.dto;

import kitchenpos.application.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;

public abstract class OrderTableChangeNumberOfGuestsRequestFixture {

    public static OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest(
            final int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }
}

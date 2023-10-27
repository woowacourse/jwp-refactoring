package kitchenpos.support.fixture.dto;

import kitchenpos.ordertable.application.dto.OrderTableChangeNumberOfGuestsRequest;

public abstract class OrderTableChangeNumberOfGuestsRequestFixture {

    public static OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest(
            final int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }
}

package kitchenpos.support.fixture.dto;

import kitchenpos.ui.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;

public class OrderTableChangeNumberOfGuestsRequestFixture {

    public static OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest(
            final int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }
}

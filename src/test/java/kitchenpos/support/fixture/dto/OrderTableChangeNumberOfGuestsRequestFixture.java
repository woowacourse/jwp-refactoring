package kitchenpos.support.fixture.dto;

import kitchenpos.ui.dto.OrderTableChangeNumberOfGuestsRequest;

public class OrderTableChangeNumberOfGuestsRequestFixture {

    public static OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest(
            final int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }
}

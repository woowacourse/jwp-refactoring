package kitchenpos.application.fixture.dto;

import kitchenpos.dto.request.EmptyRequest;
import kitchenpos.dto.request.NumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;

public class OrderTableDtoFixture {

    public static OrderTableRequest createOrderTableRequest(final int numberOfGuests, final boolean isEmpty) {
        return new OrderTableRequest(numberOfGuests, isEmpty);
    }

    public static EmptyRequest forUpdateEmpty(final boolean isEmpty) {
        return new EmptyRequest(isEmpty);
    }

    public static NumberOfGuestsRequest forUpdateGuestNumber(final int numberOfGuests) {
        return new NumberOfGuestsRequest(numberOfGuests);
    }
}

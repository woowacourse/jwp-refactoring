package kitchenpos.application.fixture.dto;

import kitchenpos.dto.request.EmptyRequest;
import kitchenpos.dto.request.NumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;

public class OrderTableDtoFixture {

    public static OrderTableResponse 빈테이블_응답 = new OrderTableResponse(1L, 1L, 3, true);
    public static OrderTableResponse 테이블_응답 = new OrderTableResponse(2L, 1L, 3, false);

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

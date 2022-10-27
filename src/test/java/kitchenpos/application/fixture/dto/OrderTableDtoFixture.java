package kitchenpos.application.fixture.dto;

import kitchenpos.dto.request.OrderTableRequest;

public class OrderTableDtoFixture {

    public static OrderTableRequest createOrderTableRequest(final int numberOfGuests, final boolean isEmpty) {
        return new OrderTableRequest(numberOfGuests, isEmpty);
    }
}

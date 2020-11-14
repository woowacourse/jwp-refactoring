package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;

public class OrderTableFixture {
    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final Long ID3 = 3L;
    public static final Long ID4 = 4L;
    public static final int DEFAULT_NUMBER_OF_GUESTS = 0;
    public static final boolean DEFAULT_EMPTY = true;

    public static OrderTable createEmptyWithId(Long id) {
        return new OrderTable(id, null, 0, true);
    }

    public static OrderTable createEmptyWithoutId() {
        return new OrderTable(null, null, 0, true);
    }

    public static OrderTable createNotEmptyWithId(Long id) {
        return new OrderTable(id, null, 1, false);
    }

    public static OrderTable createNumOf(Long id, int numOfGuests) {
        return new OrderTable(id, null, numOfGuests, false);
    }

    public static OrderTable createGroupTableWithId(Long id, Long tableGroupId) {
        return new OrderTable(id, tableGroupId, 1, true);
    }

    public static OrderTableRequest createRequest() {
        return new OrderTableRequest(DEFAULT_NUMBER_OF_GUESTS, DEFAULT_EMPTY);
    }

    public static OrderTableChangeEmptyRequest createRequestEmptyOf(boolean isEmpty) {
        return new OrderTableChangeEmptyRequest(isEmpty);
    }

    public static OrderTableChangeNumberOfGuestsRequest createRequestNumOf(int numberOfGuests) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }

    public static OrderTableResponse createResponse(Long id) {
        return OrderTableResponse.of(createNotEmptyWithId(id));
    }
}

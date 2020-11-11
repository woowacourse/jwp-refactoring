package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
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
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable createEmptyWithoutId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable createNotEmptyWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable createNumOf(Long id, int numOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(numOfGuests);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable createGroupTableWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTableRequest createRequest() {
        return new OrderTableRequest(DEFAULT_NUMBER_OF_GUESTS, DEFAULT_EMPTY);
    }

    public static OrderTableRequest createRequestNumOf(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, DEFAULT_EMPTY);
    }

    public static OrderTableRequest createRequestEmptyOf(boolean isEmpty) {
        return new OrderTableRequest(DEFAULT_NUMBER_OF_GUESTS, isEmpty);
    }

    public static OrderTableResponse createResponse(Long id) {
        return OrderTableResponse.of(createNotEmptyWithId(id));
    }
}

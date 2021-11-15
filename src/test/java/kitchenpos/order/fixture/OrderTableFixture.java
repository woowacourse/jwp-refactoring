package kitchenpos.order.fixture;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

public class OrderTableFixture {

    private static final Long ID = 1L;
    private static final Long TABLE_GROUP_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 1;
    private static final boolean EMPTY = false;

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(boolean empty) {
        return new OrderTable(NUMBER_OF_GUESTS, empty);
    }

    public static OrderTable createOrderTable() {
        return createOrderTable(NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTableRequest createOrderTableRequest() {
        return new OrderTableRequest(NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTableRequest createOrderTableRequest(boolean isEmpty) {
        return new OrderTableRequest(NUMBER_OF_GUESTS, isEmpty);
    }

    public static OrderTableRequest createOrderTableRequest(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, EMPTY);
    }

    public static OrderTableResponse createOrderTableResponse() {
        return new OrderTableResponse(ID, TABLE_GROUP_ID, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTableResponse createOrderTableResponse(Long id, OrderTableRequest request) {
        return new OrderTableResponse(id, null, request.getNumberOfGuests(), request.isEmpty());
    }
}

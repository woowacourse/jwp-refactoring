package kitchenpos;

import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    private static final Long ID = null;
    private static final int NUMBER_OF_GUESTS = 0;
    private static final boolean EMPTY = true;

    private OrderTableFixtures() {
    }

    public static OrderTable createOrderTable() {
        return createOrderTable(ID, null, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTableWithTableGroupId(Long tableGroupId) {
        return createOrderTable(ID, tableGroupId, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTableWithId(Long id) {
        return new OrderTable(id, null, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return createOrderTable(ID, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableResponse createOrderTableResponse() {
        return new OrderTableResponse(1L, 0, true);
    }
}

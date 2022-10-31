package kitchenpos;

import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixtures {

    private static final Long ID = null;
    private static final int NUMBER_OF_GUESTS = 0;
    private static final boolean EMPTY = true;

    private OrderTableFixtures() {
    }

    public static OrderTable createOrderTable() {
        return createOrderTable(ID, null, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTable(Long id) {
        return new OrderTable(id, null, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTable(TableGroup tableGroup) {
        return createOrderTable(ID, tableGroup, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return createOrderTable(ID, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTableResponse createOrderTableResponse() {
        return new OrderTableResponse(1L, 0, true);
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTableWithoutId() {
        return createOrderTable(null, null, 1, false);
    }

    public static OrderTable createOrderTableWithId(Long id) {
        return createOrderTable(id, null, 1, true);
    }

    public static OrderTable createOrderTableWithEmpty(boolean empty) {
        return createOrderTable(null, null, 1, empty);
    }

    /*
    public static OrderTable createOrderTableWithTableGroupId(Long tableGroupId) {
        return createOrderTable(null, 2, tableGroupId, false);
    }

    public static OrderTable createOrderTableWithTableGroupIdAndEmpty(Long tableGroupId, boolean empty) {
        return createOrderTable(null, 2, tableGroupId, empty);
    }

    public static OrderTable createOrderTableWithNumberOfGuest(int numberOfGuest) {
        return createOrderTable(null, numberOfGuest, null, false);
    }


     */
}

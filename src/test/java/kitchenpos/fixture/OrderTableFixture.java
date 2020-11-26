package kitchenpos.fixture;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithoutId;

public class OrderTableFixture {

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTableWithId(Long id) {
        return createOrderTable(id, createTableGroupWithoutId(), 1, false);
    }

    public static OrderTable createOrderTableWithoutId() {
        return createOrderTable(null, null, 1, false);
    }

    public static OrderTable createOrderTableWithEmpty(boolean empty) {
        return createOrderTable(null, null, 1, empty);
    }

    public static OrderTable createOrderTableWithNumberOfGuest(int numberOfGuest) {
        return createOrderTable(null, null, numberOfGuest, false);
    }

    public static OrderTable createOrderTableWithTableGroup(TableGroup tableGroup) {
        return createOrderTable(null, tableGroup, 2, false);
    }
}

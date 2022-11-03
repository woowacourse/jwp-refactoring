package kitchenpos.support.fixtures;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixtures {

    public static OrderTable createWithGuests(final TableGroup TableGroup, final int numberOfGuests) {
        return new OrderTable(null, TableGroup, numberOfGuests, false);
    }

    public static OrderTable createEmptyTable(final TableGroup TableGroup) {
        return new OrderTable(null, TableGroup, 0, true);
    }
}

package kitchenpos.test.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableFixture {

    public static OrderTable 테이블(int numberOfGuess, boolean empty) {
        return new OrderTable(numberOfGuess, empty);
    }

    public static OrderTable 테이블(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}

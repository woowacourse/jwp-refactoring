package kitchenpos.test.fixture;

import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;

public class TableFixture {

    public static OrderTable 테이블(int numberOfGuess, boolean empty) {
        return new OrderTable(numberOfGuess, empty);
    }

    public static OrderTable 테이블(
            Long tableGroupId,
            int numberOfGuests,
            boolean empty,
            TableValidator tableValidator
    ) {
        return new OrderTable(
                tableGroupId,
                numberOfGuests,
                empty,
                tableValidator
        );
    }
}

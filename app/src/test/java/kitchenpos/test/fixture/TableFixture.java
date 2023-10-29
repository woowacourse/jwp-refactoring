package kitchenpos.test.fixture;

import kitchenpos.application.TableGroupValidator;
import kitchenpos.domain.OrderTable;

public class TableFixture {

    public static OrderTable 테이블(int numberOfGuess, boolean empty) {
        return new OrderTable(numberOfGuess, empty);
    }

    public static OrderTable 테이블(
            Long tableGroupId,
            int numberOfGuests,
            boolean empty,
            TableGroupValidator tableGroupValidator
    ) {
        return new OrderTable(
                tableGroupId,
                numberOfGuests,
                empty,
                tableGroupValidator
        );
    }
}

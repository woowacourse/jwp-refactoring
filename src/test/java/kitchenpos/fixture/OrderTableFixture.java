package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable ORDER_TABLE(boolean empty, int numberOfGuests) {
        return new OrderTable(
                null,
                numberOfGuests,
                empty
        );
    }
    
    public static OrderTable ORDER_TABLE(TableGroup tableGroup, boolean empty, int numberOfGuests) {
        return new OrderTable(
                tableGroup,
                numberOfGuests,
                empty
        );
    }
}

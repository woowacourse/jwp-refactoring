package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.OrderTableUpdateNumberOfGuestsRequest;

public class TableFixture extends DomainCreator {

    public static OrderTable 빈_테이블_1번 = createOrderTable(null, null, 1, true);
    public static OrderTable 빈_테이블_2번 = createOrderTable(null, null, 2, true);
    public static OrderTable 사용중인_테이블_1번 = createOrderTable(null, null, 4, false);

    public static OrderTableUpdateEmptyRequest createRequestEmpty(final boolean empty) {
        return new OrderTableUpdateEmptyRequest(empty);
    }

    public static OrderTableUpdateNumberOfGuestsRequest createRequestNumberOfGuests(
        final int numberOfGuests) {
        return new OrderTableUpdateNumberOfGuestsRequest(numberOfGuests);
    }
}

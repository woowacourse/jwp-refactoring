package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture extends DomainCreator {

    public static OrderTable 빈_테이블_1번 = createOrderTable(null, null, 1, true);
    public static OrderTable 빈_테이블_2번 = createOrderTable(null, null, 2, true);
    public static OrderTable 사용중인_테이블_1번 = createOrderTable(null, null, 4, false);

    public static OrderTable createRequestEmpty(boolean empty) {
        return createOrderTable(null, null, 0, empty);
    }

    public static OrderTable createRequestNumberOfGuests(int numberOfGuests) {
        return createOrderTable(null, null, numberOfGuests, true);
    }
}

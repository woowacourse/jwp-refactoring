package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문테이블_N명(final int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

    public static OrderTable 빈테이블_1명() {
        return new OrderTable(1, true);
    }

    public static OrderTable 빈테이블_1명_단체지정() {
        return new OrderTable(TableGroupFixture.단체지정_빈테이블_1개(), 1, true);
    }
}

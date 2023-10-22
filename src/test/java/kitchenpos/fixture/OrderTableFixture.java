package kitchenpos.fixture;

import java.util.List;
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
        return new OrderTable(TableGroupFixture.단체지정_여러_테이블(List.of(빈테이블_1명(), 빈테이블_1명())), 1, true);
    }
}

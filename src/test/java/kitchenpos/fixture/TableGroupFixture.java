package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체지정_빈테이블_2개() {
        return new TableGroup(LocalDateTime.now(), List.of(OrderTableFixture.빈테이블_1명(), OrderTableFixture.빈테이블_1명()));
    }

    public static TableGroup 단체지정_빈테이블_1개() {
        return new TableGroup(LocalDateTime.now(), Collections.singletonList(OrderTableFixture.빈테이블_1명()));
    }

    public static TableGroup 단체지정_주문테이블_2개() {
        return new TableGroup(LocalDateTime.now(),
                List.of(OrderTableFixture.주문테이블_N명(1), OrderTableFixture.주문테이블_N명(5)));
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.TableGroup;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체지정_빈테이블_2개() {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(OrderTableFixture.빈테이블_1명(), OrderTableFixture.빈테이블_1명()));
        return tableGroup;
    }

    public static TableGroup 단체지정_빈테이블_1개() {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(OrderTableFixture.빈테이블_1명()));
        return tableGroup;
    }

    public static TableGroup 단체지정_주문테이블_2개() {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(OrderTableFixture.주문테이블_1명(), OrderTableFixture.주문테이블_5명()));
        return tableGroup;
    }
}

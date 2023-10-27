package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 테이블_그룹1() {
        OrderTable 빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        OrderTable 빈_신규_테이블2 = OrderTableFixture.빈_신규_테이블2();
        return new TableGroup(1L, LocalDateTime.now(), List.of(빈_신규_테이블1, 빈_신규_테이블2));
    }
}

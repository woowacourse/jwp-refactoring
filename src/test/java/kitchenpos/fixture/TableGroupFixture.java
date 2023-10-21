package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 오더_테이블이_있는_테이블_그룹_생성(List<OrderTable> savedOrderTables) {
        TableGroup tableGroup = TableGroup.from();
        tableGroup.addAllOrderTables(savedOrderTables);

        return tableGroup;
    }

    public static TableGroup 빈_테이블_그룹_생성() {
        return TableGroup.from();
    }

}

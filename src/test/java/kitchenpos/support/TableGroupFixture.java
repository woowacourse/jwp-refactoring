package kitchenpos.support;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹_구성(final OrderTable... orderTables){
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTables));
        return tableGroup;
    }
}

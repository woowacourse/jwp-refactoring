package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    // TODO: 체크
    public static TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.createDefault();
        return tableGroup;
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final TableGroup tableGroup) {
        return new TableGroupCreateRequest(null);
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final List<Long> ids) {
        return new TableGroupCreateRequest(ids);
    }
}

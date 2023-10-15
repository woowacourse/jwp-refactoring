package kitchenpos.fixture;

import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.createDefault();
        tableGroup.initOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return new TableGroupCreateRequest(orderTableIds);
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final List<Long> ids) {
        return new TableGroupCreateRequest(ids);
    }
}

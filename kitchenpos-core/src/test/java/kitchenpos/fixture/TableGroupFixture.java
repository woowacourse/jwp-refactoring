package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체지정_여러_테이블(final List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroupCreateRequest 단체지정요청_생성(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIds);
    }
}

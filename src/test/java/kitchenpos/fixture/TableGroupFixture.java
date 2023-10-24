package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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

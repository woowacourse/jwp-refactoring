package kitchenpos.fixture;

import kitchenpos.table.application.dto.request.OrderTableIdRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;

import java.util.List;

public class TableGroupFixtures {

    public static TableGroupCreateRequest createTableGroup(final List<OrderTableIdRequest> orderTables) {
        return new TableGroupCreateRequest(orderTables);
    }
}

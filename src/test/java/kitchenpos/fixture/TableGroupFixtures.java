package kitchenpos.fixture;

import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupCreateRequest;

import java.util.List;

public class TableGroupFixtures {

    public static TableGroupCreateRequest createTableGroup(final List<OrderTableIdRequest> orderTables) {
        return new TableGroupCreateRequest(orderTables);
    }
}

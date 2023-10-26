package kitchenpos.common.fixtures;

import java.util.List;
import kitchenpos.ordertable.application.dto.OrderTableFindRequest;
import kitchenpos.ordertable.application.dto.TableGroupCreateRequest;

public class TableGroupFixtures {

    /**
     * ORDER_TABLES
     */
    public static List<OrderTableFindRequest> TABLE_GROUP1_ORDER_TABLE_REQUESTS() {
        final OrderTableFindRequest orderTableFindRequest1 = new OrderTableFindRequest(1L);
        final OrderTableFindRequest orderTableFindRequest2 = new OrderTableFindRequest(2L);

        return List.of(orderTableFindRequest1, orderTableFindRequest2);
    }

    /**
     * REQUEST
     */
    public static TableGroupCreateRequest TABLE_GROUP1_CREATE_REQUEST() {
        List<OrderTableFindRequest> orderTables = TABLE_GROUP1_ORDER_TABLE_REQUESTS();
        return new TableGroupCreateRequest(orderTables);
    }
}

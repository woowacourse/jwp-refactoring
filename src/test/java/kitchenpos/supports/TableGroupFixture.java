package kitchenpos.supports;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.table.service.dto.TableResponse;

public class TableGroupFixture {

    public static TableGroupRequest from(final TableResponse... orderTables) {
        final List<OrderTableIdRequest> tableIds = Arrays.stream(orderTables)
                .map(each -> new OrderTableIdRequest(each.getId()))
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIds);
    }

    public static TableGroupRequest fromIds(final Long... tableIds) {
        final List<OrderTableIdRequest> tableIdRequests = Arrays.stream(tableIds)
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIdRequests);
    }
}

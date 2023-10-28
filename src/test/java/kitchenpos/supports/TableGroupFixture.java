package kitchenpos.supports;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.service.dto.TableIdRequest;
import kitchenpos.order.service.dto.TableGroupRequest;
import kitchenpos.order.service.dto.TableResponse;

public class TableGroupFixture {

    public static TableGroupRequest from(final TableResponse... orderTables) {
        final List<TableIdRequest> tableIds = Arrays.stream(orderTables)
                .map(each -> new TableIdRequest(each.getId()))
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIds);
    }

    public static TableGroupRequest fromIds(final Long... tableIds) {
        final List<TableIdRequest> tableIdRequests = Arrays.stream(tableIds)
                .map(TableIdRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIdRequests);
    }
}

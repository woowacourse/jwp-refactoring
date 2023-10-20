package kitchenpos.fixture;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.application.dto.TableGroupRequest.OrderTableIdRequest;

public class TableGroupFixture {

    public static TableGroupRequest tableGroupRequest(List<OrderTableIdRequest> orderTableIdRequests) {
        return new TableGroupRequest(orderTableIdRequests);
    }

    public static TableGroup tableGroup(List<OrderTable> orderTable) {
        return new TableGroup(LocalDateTime.now(), orderTable);
    }

    public static TableGroup tableGroupWithoutOrderTable(LocalDateTime createdDate) {
        return new TableGroup(createdDate, new ArrayList<>());
    }
}

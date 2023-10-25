package core.fixture;

import core.application.dto.TableGroupRequest;
import core.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static core.application.dto.TableGroupRequest.OrderTableIdRequest;

public class TableGroupFixture {

    public static TableGroupRequest tableGroupRequest(List<OrderTableIdRequest> orderTableIdRequests) {
        return new TableGroupRequest(orderTableIdRequests);
    }

    public static TableGroup tableGroup() {
        return new TableGroup(LocalDateTime.now());
    }

    public static TableGroup tableGroupWithoutOrderTable(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }
}

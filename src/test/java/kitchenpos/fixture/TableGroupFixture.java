package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;

public class TableGroupFixture {
    public static TableGroup tableGroup() {
        return new TableGroup(0L, LocalDateTime.now(),
                Arrays.asList(OrderTableFixture.orderTable(),
                        OrderTableFixture.orderTable()));
    }

    public static TableGroupRequest tableGroupRequest() {
        return new TableGroupRequest(Arrays.asList(OrderTableFixture.orderTable(),
                OrderTableFixture.orderTable()));
    }
}

package kitchenpos;

import static kitchenpos.OrderTableFixtures.createOrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    private TableGroupFixtures() {
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(LocalDateTime.now(), List.of(1L, 2L));
    }

    public static TableGroup createTableGroup(Long... orderTableIds) {
        return createTableGroup(LocalDateTime.now(), List.of(orderTableIds));
    }

    public static TableGroup createTableGroup(LocalDateTime createdDateTime, List<Long> orderTableIds) {
        return new TableGroup(createdDateTime, orderTableIds);
    }

    public static TableGroupResponse createTableGroupResponse() {
        return new TableGroupResponse(
                1L,
                LocalDateTime.now(),
                List.of(createOrderTableResponse(), createOrderTableResponse())
        );
    }
}

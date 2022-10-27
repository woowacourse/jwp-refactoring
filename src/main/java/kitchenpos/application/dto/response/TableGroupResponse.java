package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public record TableGroupResponse(Long id, LocalDateTime createDate, List<OrderTableResponse> orderTables) {

    public static TableGroupResponse from(final TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::from)
                .toList();
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }
}

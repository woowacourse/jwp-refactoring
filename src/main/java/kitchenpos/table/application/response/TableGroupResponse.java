package kitchenpos.table.application.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createDate;
    private List<OrderTableResponse> orderTableResponses;

    public TableGroupResponse(Long id, LocalDateTime createDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createDate = createDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(),
                tableGroup.getCreatedDate(),
                toTableGroupResponse(tableGroup));
    }

    private static List<OrderTableResponse> toTableGroupResponse(TableGroup tableGroup) {
        return tableGroup.getOrderTables().getValues().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}

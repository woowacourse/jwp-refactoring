package kitchenpos.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableResponse;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    protected TableGroupResponse() {
    }

    private TableGroupResponse(final Long id, final LocalDateTime createdDate,
            List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse of(final TableGroup tableGroup, final List<OrderTableResponse> orderTableResponses) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}

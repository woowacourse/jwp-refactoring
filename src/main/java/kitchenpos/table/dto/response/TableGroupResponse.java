package kitchenpos.table.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.toList(orderTables)
        );
    }

    protected TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate,
                               List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }

    public void setOrderTableResponses(final List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }
}

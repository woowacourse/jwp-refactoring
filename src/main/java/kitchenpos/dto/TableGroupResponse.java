package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
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

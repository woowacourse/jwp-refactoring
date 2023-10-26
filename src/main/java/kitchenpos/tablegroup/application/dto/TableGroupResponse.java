package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<Long> orderTableIds) {

        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableIds
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}

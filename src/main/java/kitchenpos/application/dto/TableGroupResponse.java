package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    public TableGroupResponse(final Long id,
                              final LocalDateTime createDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createDate = createDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse createResponse(final TableGroup tableGroup) {
        return new TableGroupResponse(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            tableGroup.getOrderTables().getOrderTables()
                .stream()
                .map(OrderTableResponse::createResponse)
                .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}

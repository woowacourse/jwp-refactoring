package kitchenpos.application.response;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    @JsonCreator
    public TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupResponse(final TableGroup tableGroup, final List<OrderTableResponse> orderTables) {
        this(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        final List<OrderTableResponse> orderTables = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::new)
                .collect(toList());

        return new TableGroupResponse(tableGroup, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}

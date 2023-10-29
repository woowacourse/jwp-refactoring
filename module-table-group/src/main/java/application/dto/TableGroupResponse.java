package application.dto;

import domain.TableGroup;
import java.time.LocalDateTime;
import java.util.List;


public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<Long> orderTables;

    public TableGroupResponse(
            final Long id,
            final LocalDateTime createdDate,
            final List<Long> orderTables
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup, final List<Long> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}

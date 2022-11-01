package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<TableResponse> tableRespons;

    public TableGroupResponse(final Long id,
                              final LocalDateTime createdDate,
                              final List<TableResponse> tableRespons) {
        this.id = id;
        this.createdDate = createdDate;
        this.tableRespons = tableRespons;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getOrderTableResponses() {
        return tableRespons;
    }
}

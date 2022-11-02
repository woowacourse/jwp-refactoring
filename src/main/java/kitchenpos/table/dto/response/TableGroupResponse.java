package kitchenpos.table.dto.response;

import java.time.LocalDateTime;

import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public TableGroupResponse(TableGroup tableGroup) {
        id = tableGroup.getId();
        createdDate = tableGroup.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}

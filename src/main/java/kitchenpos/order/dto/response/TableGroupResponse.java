package kitchenpos.order.dto.response;

import java.time.LocalDateTime;

import kitchenpos.order.domain.TableGroup;

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

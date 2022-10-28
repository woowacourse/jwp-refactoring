package kitchenpos.dto.response;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public TableGroupResponse(TableGroup tableGroup) {
        id = tableGroup.getId();
        createdDate = tableGroup.getCreatedDate();
    }
}

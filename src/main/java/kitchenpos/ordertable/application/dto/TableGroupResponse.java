package kitchenpos.ordertable.application.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return new TableGroupResponse(null, null);
        }
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import kitchenpos.domain.tablegroup.TableGroup;

public class CreateTableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public CreateTableGroupResponse(final TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import kitchenpos.application.dto.CreateTableGroupDto;

public class CreateTableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public CreateTableGroupResponse(final CreateTableGroupDto dto) {
        this.id = dto.getId();
        this.createdDate = dto.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

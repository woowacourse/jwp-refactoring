package kitchenpos.application.dto;

import java.time.LocalDateTime;
import kitchenpos.domain.tablegroup.TableGroup;

public class CreateTableGroupDto {

    private Long id;
    private LocalDateTime createdDate;

    public CreateTableGroupDto(final TableGroup tableGroup) {
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

package kitchenpos.tablegroup.persistence.dto;

import java.time.LocalDateTime;

public class TableGroupDataDto {

    private Long id;
    private LocalDateTime createdDate;

    public TableGroupDataDto(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

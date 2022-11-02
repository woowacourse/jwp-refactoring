package kitchenpos.table.dao;

import java.time.LocalDateTime;
import kitchenpos.table.domain.TableGroup;

public class TableGroupDto {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupDto(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup toEntity() {
        return new TableGroup(id, createdDate);
    }
}

package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResult {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupResult(
            final Long id,
            final LocalDateTime createdDate
    ) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResult from(final TableGroup tableGroup) {
        return new TableGroupResult(
                tableGroup.getId(),
                tableGroup.getCreatedDate()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

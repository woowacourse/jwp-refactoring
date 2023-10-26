package kitchenpos.table_group.application.entity;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table_group.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupEntity {

    private Long id;
    private LocalDateTime createdDate;

    public TableGroupEntity(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupEntity() {
    }

    public static TableGroupEntity from(final TableGroup tableGroup) {
        return new TableGroupEntity(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public TableGroup toTableGroup(final OrderTables orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupDto {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<Long> orderTableIds;

    public static TableGroupDto from(TableGroup tableGroup) {
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTableIds());
    }

    public TableGroupDto(LocalDateTime createdDate, List<Long> orderTableIds) {
        this(null, createdDate, orderTableIds);
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toDomain() {
        return new TableGroup(id, createdDate, orderTableIds);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}

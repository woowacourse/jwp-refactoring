package kitchenpos.application.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableOfGroupDto> orderTables;

    private TableGroupResponse() {}

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<TableOfGroupDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup tableGroup, final List<Long> orderTableIds) {
        final List<TableOfGroupDto> tablesOfGroup = orderTableIds.stream()
            .map(TableOfGroupDto::from)
            .collect(Collectors.toList());

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tablesOfGroup);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableOfGroupDto> getOrderTables() {
        return orderTables;
    }
}

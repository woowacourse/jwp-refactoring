package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableDto> orderTables;

    public TableGroupDto(final Long id, final LocalDateTime createdDate, final List<TableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto toDto(final TableGroup tableGroup) {
        final List<TableDto> tableDtos = tableGroup.getOrderTables()
                .stream()
                .map(TableDto::toDto)
                .collect(Collectors.toList());
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), tableDtos);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableDto> getOrderTables() {
        return orderTables;
    }
}

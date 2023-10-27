package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.application.dto.TableDto;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableDto> orderTables;

    public TableGroupDto(final Long id, final LocalDateTime createdDate, final List<TableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto toDto(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        final List<TableDto> tableDtos = orderTables
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

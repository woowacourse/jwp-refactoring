package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableDto> orderTables;

    private TableGroupDto() {
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<TableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        List<TableDto> orderTableDtos = orderTables
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableDtos);
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

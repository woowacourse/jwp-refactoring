package kitchenpos.application.table.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.domain.table.OrderTable;
import kitchenpos.core.domain.table.TableGroup;

public class TableGroupDto {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<TableDto> orderTables;

    public TableGroupDto(Long id, LocalDateTime createdDate, List<TableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup, List<OrderTable> orderTableList) {
        List<TableDto> orderTables = orderTableList
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
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

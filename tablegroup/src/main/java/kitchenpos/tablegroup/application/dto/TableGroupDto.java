package kitchenpos.tablegroup.application.dto;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.application.dto.OrderTableDto;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupDto {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableDto> orderTables;

    private TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup, List<OrderTable> orderTables) {
        Long id = tableGroup.getId();
        LocalDateTime createdDate = tableGroup.getCreatedDate();
        List<OrderTableDto> orderTablesDto = orderTables.stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
        return new TableGroupDto(id, createdDate, orderTablesDto);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}

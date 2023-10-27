package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.application.OrderTableDto;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public static TableGroupDto from(final TableGroup tableGroup) {
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream().map(OrderTableDto::from)
                        .collect(java.util.stream.Collectors.toList()));
    }

    public TableGroupDto(final Long id, final LocalDateTime createdDate, final List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

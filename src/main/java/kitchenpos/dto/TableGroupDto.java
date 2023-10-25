package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public static TableGroupDto from(TableGroup tableGroup) {
        List<OrderTableDto> orderTableDtos = tableGroup.getOrderTables().stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableDtos);
    }

    public TableGroupDto(List<OrderTableDto> orderTables) {
        this(null, null, orderTables);
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
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

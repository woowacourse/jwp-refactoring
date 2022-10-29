package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableDto> orderTables;

    public TableGroupDto(final Long id,
                         final LocalDateTime createdDate,
                         final List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto from(final TableGroup tableGroup) {
        final List<OrderTableDto> orderTableDtos = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());

        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableDtos);
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

    @Override
    public String toString() {
        return "TableGroupDto{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}

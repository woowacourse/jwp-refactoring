package kitchenpos.table_group.application.dto;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table_group.domain.TableGroup;

public class TableGroupDto {

    private final Long id;

    private final LocalDateTime createdDate;

    private final List<OrderTableDtoInTableGroup> orderTables;

    public TableGroupDto(
        final Long id,
        final LocalDateTime createdDate,
        final List<OrderTableDtoInTableGroup> orderTables
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto createResponse(
        final TableGroup tableGroup,
        final List<OrderTableDtoInTableGroup> orderTableDtos
    ) {
        return new TableGroupDto(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            null
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return createdDate;
    }

    public List<OrderTableDtoInTableGroup> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableDtoInTableGroup::getId)
            .collect(toUnmodifiableList());
    }

    public TableGroup toTableGroup() {
        return new TableGroup(createdDate);
    }
}

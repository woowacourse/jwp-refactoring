package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;

public class TableGroupDto {

    private final Long id;

    private final LocalDateTime localDateTime;

    private final List<OrderTableDto> orderTables;

    public TableGroupDto(
        final Long id,
        final LocalDateTime localDateTime,
        final List<OrderTableDto> orderTables
    ) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.orderTables = orderTables;
    }

    public static final TableGroupDto from(final TableGroup tableGroup) {
        final List<OrderTableDto> orderTableDtos = tableGroup.getOrderTables()
            .stream()
            .map(OrderTableDto::from)
            .collect(Collectors.toList());

        return new TableGroupDto(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            orderTableDtos
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}

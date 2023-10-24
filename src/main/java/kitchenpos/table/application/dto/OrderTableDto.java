package kitchenpos.table.application.dto;

import java.util.Optional;
import kitchenpos.table.domain.OrderTable;

public class OrderTableDto {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableDto(
        final Long id,
        final Long tableGroupId,
        final Integer numberOfGuests,
        final Boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableDto from(final OrderTable orderTable) {
        return Optional.ofNullable(orderTable.getTableGroup())
            .map(tableGroup -> createOrderTableDto(orderTable, tableGroup.getId()))
            .orElseGet(() -> createOrderTableDto(orderTable, null));
    }

    private static OrderTableDto createOrderTableDto(final OrderTable orderTable,
        final Long tableGroupId) {
        return new OrderTableDto(
            orderTable.getId(),
            tableGroupId,
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

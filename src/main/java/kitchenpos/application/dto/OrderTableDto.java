package kitchenpos.application.dto;

import kitchenpos.domain.order.OrderTable;

import java.util.Objects;

public class OrderTableDto {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableDto(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableDto from(OrderTable orderTable) {
        Long tableGroupId = null;
        if (Objects.nonNull(orderTable.getTableGroup())) {
            tableGroupId = orderTable.getTableGroup().getId();
        }
        return new OrderTableDto(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests(),
                orderTable.getEmpty());
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

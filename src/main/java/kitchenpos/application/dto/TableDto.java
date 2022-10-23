package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;
import lombok.Getter;

@Getter
public class TableDto {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    public TableDto(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableDto of(OrderTable orderTable) {
        return new TableDto(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.getEmpty());
    }
}

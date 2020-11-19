package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private LocalDateTime tableGroupCreatedDate;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, LocalDateTime tableGroupCreatedDate,
                               int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.tableGroupCreatedDate = tableGroupCreatedDate;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();
        return new OrderTableResponse(orderTable.getId(), tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public LocalDateTime getTableGroupCreatedDate() {
        return tableGroupCreatedDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

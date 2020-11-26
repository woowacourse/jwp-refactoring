package kitchenpos.ordertable.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.model.OrderTable;

public class OrderTableResponseDto {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public static OrderTableResponseDto from(OrderTable orderTable) {
        return new OrderTableResponseDto(orderTable.getId(), orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty());
    }

    public static List<OrderTableResponseDto> listOf(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponseDto::from)
            .collect(Collectors.toList());
    }

    public OrderTableResponseDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

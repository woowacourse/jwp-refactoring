package kitchenpos.application.dto.response.table;

import kitchenpos.domain.OrderTable;

public class OrderTableResponseDto {

    private Long id;
    private int numberOfGuests;
    private TableGroupResponseDto tableGroupResponseDto;
    private boolean empty;

    private OrderTableResponseDto() {
    }

    public OrderTableResponseDto(
        Long id,
        int numberOfGuests,
        TableGroupResponseDto tableGroupResponseDto,
        boolean empty
    ) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableGroupResponseDto = tableGroupResponseDto;
        this.empty = empty;
    }

    public static OrderTableResponseDto from(OrderTable orderTable) {
        return new OrderTableResponseDto(orderTable.getId(), orderTable.getNumberOfGuests(), TableGroupResponseDto.from(orderTable.getTableGroup()), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroupResponseDto getTableGroupResponseDto() {
        return tableGroupResponseDto;
    }

    public boolean isEmpty() {
        return empty;
    }
}

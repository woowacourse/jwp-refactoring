package kitchenpos.ui.dto.response.table;

import kitchenpos.application.dto.response.table.OrderTableResponseDto;
import kitchenpos.ui.dto.response.tablegroup.TableGroupResponse;

public class OrderTableResponse {

    private Long id;
    private int numberOfGuests;
    private TableGroupResponse tableGroupResponse;
    private boolean empty;

    private OrderTableResponse() {
    }

    public OrderTableResponse(Long id, int numberOfGuests, TableGroupResponse tableGroupResponse, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableGroupResponse = tableGroupResponse;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTableResponseDto orderTableResponseDto) {
        return new OrderTableResponse(orderTableResponseDto.getId(),
            orderTableResponseDto.getNumberOfGuests(),
            TableGroupResponse.from(orderTableResponseDto.getTableGroupResponseDto()),
            orderTableResponseDto.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroupResponse getTableGroupResponse() {
        return tableGroupResponse;
    }

    public boolean isEmpty() {
        return empty;
    }
}

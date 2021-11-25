package kitchenpos.table.ui.dto.response;

import java.util.List;

public class OrderTablesResponseDto {

    private List<OrderTableResponseDto> orderTables;

    private OrderTablesResponseDto() {
    }

    public OrderTablesResponseDto(
        List<OrderTableResponseDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableResponseDto> getOrderTables() {
        return orderTables;
    }
}

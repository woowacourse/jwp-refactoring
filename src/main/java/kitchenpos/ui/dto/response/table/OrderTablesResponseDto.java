package kitchenpos.ui.dto.response.table;

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

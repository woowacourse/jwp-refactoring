package kitchenpos.application.dto;

import java.util.List;

public class CreateTableGroupDto {

    private List<CreateOrderTableIdDto> orderTables;

    public CreateTableGroupDto(List<CreateOrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<CreateOrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}

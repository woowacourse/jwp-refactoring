package kitchenpos.tablegroup.application.dto;

import kitchenpos.ordertable.application.dto.CreateOrderTableIdDto;

import java.util.List;

public class CreateTableGroupDto {

    private List<CreateOrderTableIdDto> orderTables;

    public CreateTableGroupDto() {
    }

    public CreateTableGroupDto(List<CreateOrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<CreateOrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}

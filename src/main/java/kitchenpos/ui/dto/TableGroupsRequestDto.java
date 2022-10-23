package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateTableGroupDto;

public class TableGroupsRequestDto {

    private List<OrderTableIdRequestDto> orderTables;

    public TableGroupsRequestDto() {
    }

    public CreateTableGroupDto toCreateTableGroupDto() {
        return new CreateTableGroupDto(orderTables.stream()
                .map(it -> it.id)
                .collect(Collectors.toList()));
    }

    public void setOrderTables(List<OrderTableIdRequestDto> orderTables) {
        this.orderTables = orderTables;
    }

    static class OrderTableIdRequestDto {

        private Long id;

        public OrderTableIdRequestDto() {
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}

package kitchenpos.ui.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.dto.request.CreateTableGroupDto;

public class TableGroupsRequestDto {

    private List<OrderTableIdRequestDto> orderTables;

    public TableGroupsRequestDto() {
    }

    public CreateTableGroupDto toCreateTableGroupDto() {
        return new CreateTableGroupDto(orderTables.stream()
                .map(it -> it.id)
                .collect(Collectors.toList()));
    }

    public List<OrderTableIdRequestDto> getOrderTables() {
        return orderTables;
    }

    static class OrderTableIdRequestDto {

        private Long id;

        public OrderTableIdRequestDto() {
        }

        public Long getId() {
            return id;
        }
    }
}

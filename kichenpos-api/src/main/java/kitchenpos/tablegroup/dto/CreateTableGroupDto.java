package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupDto {

    private final List<CreateTableGroupOrderTableDto> orderTables;

    public CreateTableGroupDto(final List<CreateTableGroupOrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                          .map(CreateTableGroupOrderTableDto::getId)
                          .collect(Collectors.toList());
    }
}

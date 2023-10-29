package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupRequest {

    private List<CreateTableGroupOrderTableRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupDto toCreateTableGroupDto() {
        final List<CreateTableGroupOrderTableDto> ordertableDtos = orderTables.stream()
                                                                       .map(CreateTableGroupOrderTableRequest::toCreateTableGroupOrderTableDto)
                                                                       .collect(Collectors.toList());
        return new CreateTableGroupDto(ordertableDtos);
    }
}

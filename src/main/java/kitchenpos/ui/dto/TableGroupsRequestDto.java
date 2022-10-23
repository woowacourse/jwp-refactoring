package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateTableGroupDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableGroupsRequestDto {

    private List<OrderTableIdRequestDto> orderTables;

   public CreateTableGroupDto toCreateTableGroupDto() {
        return new CreateTableGroupDto(orderTables.stream()
                .map(it -> it.id)
                .collect(Collectors.toList()));
    }

    @NoArgsConstructor
    static class OrderTableIdRequestDto {

        Long id;
    }
}

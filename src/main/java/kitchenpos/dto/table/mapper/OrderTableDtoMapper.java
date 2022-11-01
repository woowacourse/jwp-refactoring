package kitchenpos.dto.table.mapper;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.response.OrderTableResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderTableDtoMapper {

    @Mapping(target = "tableGroupId", expression = "java(orderTable.getTableGroup() != null ? orderTable.getTableGroup().getId() : null)")
    OrderTableResponse toOrderTableResponse(OrderTable orderTable);

    List<OrderTableResponse> toOrderTableResponses(List<OrderTable> orderTables);
}

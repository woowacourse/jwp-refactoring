package kitchenpos.ui.mapper;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.response.OrderTableCreateResponse;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.OrderTableUpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderTableDtoMapper {

    OrderTableCreateResponse toOrderTableCreateResponse(OrderTable orderTable);

    @Mapping(target = "tableGroupId", expression = "java(orderTable.getTableGroup() != null ? orderTable.getTableGroup().getId() : null)")
    OrderTableResponse toOrderTableResponse(OrderTable orderTable);

    @Mapping(target = "tableGroupId", expression = "java(orderTable.getTableGroup() != null ? orderTable.getTableGroup().getId() : null)")
    OrderTableUpdateResponse toOrderTableUpdateResponse(OrderTable orderTable);

    List<OrderTableResponse> toOrderTableResponses(List<OrderTable> orderTables);
}

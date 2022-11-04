package kitchenpos.dto.table.mapper;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.response.OrderTableResponse;

public interface OrderTableDtoMapper {

    OrderTableResponse toOrderTableResponse(OrderTable orderTable);

    List<OrderTableResponse> toOrderTableResponses(List<OrderTable> orderTables);
}

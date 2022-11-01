package kitchenpos.dto.table.mapper;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.request.OrderTableCreateRequest;

public interface OrderTableMapper {

    OrderTable toOrderTable(OrderTableCreateRequest orderTableCreateRequest);
}

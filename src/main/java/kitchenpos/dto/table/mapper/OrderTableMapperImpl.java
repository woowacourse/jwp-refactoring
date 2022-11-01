package kitchenpos.dto.table.mapper;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.request.OrderTableCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderTableMapperImpl implements OrderTableMapper {

    @Override
    public OrderTable toOrderTable(final OrderTableCreateRequest orderTableCreateRequest) {
        return new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.isEmpty());
    }
}

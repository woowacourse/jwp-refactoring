package kitchenpos.dto.table.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.response.OrderTableResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderTableDtoMapperImpl implements OrderTableDtoMapper {

    @Override
    public OrderTableResponse toOrderTableResponse(final OrderTable orderTable) {
        if (orderTable.getTableGroup() == null) {
            return new OrderTableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup().getId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    @Override
    public List<OrderTableResponse> toOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }
}

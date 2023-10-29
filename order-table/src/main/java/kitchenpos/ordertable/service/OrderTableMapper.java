package kitchenpos.ordertable.service;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderTableMapper {

    public OrderTable toEntity(OrderTableDto orderTableDto) {
        return new OrderTable.Builder()
            .setId(orderTableDto.getId())
            .setTableGroupId(orderTableDto.getTableGroupId())
            .setNumberOfGuests(orderTableDto.getNumberOfGuests())
            .setEmpty(orderTableDto.isEmpty())
            .build();
    }
}

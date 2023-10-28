package kitchenpos.ordertable.service;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableMapper {

    public OrderTable toEntity(OrderTableDto orderTableDto) {
        return new OrderTable.Builder()
            .setNumberOfGuests(orderTableDto.getNumberOfGuests())
            .setEmpty(orderTableDto.isEmpty())
            .build();
    }
}

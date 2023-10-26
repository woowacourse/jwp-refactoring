package kitchenpos.ordertable.domain.repository.converter;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.persistence.dto.OrderTableDataDto;
import kitchenpos.support.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderTableConverter implements Converter<OrderTable, OrderTableDataDto> {

    @Override
    public OrderTableDataDto entityToData(final OrderTable orderTable) {
        return new OrderTableDataDto(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    @Override
    public OrderTable dataToEntity(final OrderTableDataDto orderTableDataDto) {
        return OrderTable.builder(orderTableDataDto.getNumberOfGuests(), orderTableDataDto.isEmpty())
                .id(orderTableDataDto.getId())
                .tableGroupId(orderTableDataDto.getTableGroupId())
                .build();
    }
}

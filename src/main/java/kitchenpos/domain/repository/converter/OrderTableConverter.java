package kitchenpos.domain.repository.converter;

import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.dto.OrderTableDataDto;
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

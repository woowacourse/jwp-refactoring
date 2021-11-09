package kitchenpos.table.service;

import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {
    public OrderTable mapFrom(TableRequest tableRequest) {
        return new OrderTable(
                tableRequest.getNumberOfGuests(),
                tableRequest.getEmpty()
        );
    }
}

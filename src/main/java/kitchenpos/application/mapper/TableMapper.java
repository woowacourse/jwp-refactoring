package kitchenpos.application.mapper;

import kitchenpos.application.dto.TableRequest;
import kitchenpos.domain.OrderTable;
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

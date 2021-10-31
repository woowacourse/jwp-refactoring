package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.TableRequest;
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

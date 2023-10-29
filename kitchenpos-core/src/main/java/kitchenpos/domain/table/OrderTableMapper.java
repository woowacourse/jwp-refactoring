package kitchenpos.domain.table;

import kitchenpos.dto.request.TableCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public OrderTable toOrderTable(final TableCreateRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }
}

package kitchenpos.domain.table;

import kitchenpos.dto.request.CreateOrderTableRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public OrderTable toOrderTable(CreateOrderTableRequest request) {
        return OrderTable.builder()
                .numberOfGuests(request.getNumberOfGuests())
                .empty(request.isEmpty())
                .build();
    }
}

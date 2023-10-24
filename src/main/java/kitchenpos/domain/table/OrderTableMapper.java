package kitchenpos.domain.table;

import kitchenpos.dto.request.CreateOrderTableRequest;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable toOrderTable(CreateOrderTableRequest request) {
        return OrderTable.builder()
                .numberOfGuests(request.getNumberOfGuests())
                .empty(request.isEmpty())
                .build();
    }
}

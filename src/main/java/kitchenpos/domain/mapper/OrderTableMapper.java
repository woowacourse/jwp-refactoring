package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.domain.OrderTable;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable toOrderTable(CreateOrderTableRequest request) {
        return OrderTable.builder()
                .id(request.getId())
                .tableGroupId(request.getTableGroupId())
                .numberOfGuests(request.getNumberOfGuests())
                .empty(request.isEmpty())
                .build();
    }
}

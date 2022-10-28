package kitchenpos.support.fixture.dto;

import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableSaveRequest;
import kitchenpos.table.domain.OrderTable;

public class OrderTableDtoFixture {

    public static OrderTableSaveRequest 상품_생성_요청(OrderTable orderTable) {
        return new OrderTableSaveRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static OrderTableResponse 상품_생성_응답(OrderTable orderTable) {
        return OrderTableResponse.toResponse(orderTable);
    }
}

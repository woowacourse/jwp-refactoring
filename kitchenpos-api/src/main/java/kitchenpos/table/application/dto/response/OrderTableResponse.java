package kitchenpos.table.application.dto.response;

import kitchenpos.table.domain.OrderTable;

public record OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}

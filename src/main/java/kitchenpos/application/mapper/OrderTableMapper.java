package kitchenpos.application.mapper;

import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;

import java.util.Objects;

public class OrderTableMapper {

    public static OrderTableResponse mapToOrderTableResponseBy(final OrderTable orderTable) {
        Long tableGroupId = null;
        if (Objects.nonNull(orderTable.getTableGroup())) {
            tableGroupId = orderTable.getTableGroup().getId();
        }

        return new OrderTableResponse(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
}

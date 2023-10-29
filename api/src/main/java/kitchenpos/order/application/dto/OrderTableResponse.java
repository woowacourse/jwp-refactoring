package kitchenpos.order.application.dto;

import kitchenpos.order.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;

    private OrderTableResponse(final Long id, final Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableResponse of(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}

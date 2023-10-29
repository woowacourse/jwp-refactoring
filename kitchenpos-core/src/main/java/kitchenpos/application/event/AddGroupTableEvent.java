package kitchenpos.application.event;

import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableDto;

import java.util.List;

public class AddGroupTableEvent {

    private final TableGroup tableGroup;
    private final List<OrderTableDto> orderTableDtos;

    public AddGroupTableEvent(final TableGroup tableGroup, final List<OrderTableDto> orderTableDtos) {
        this.tableGroup = tableGroup;
        this.orderTableDtos = orderTableDtos;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<OrderTableDto> getOrderTableDtos() {
        return orderTableDtos;
    }
}

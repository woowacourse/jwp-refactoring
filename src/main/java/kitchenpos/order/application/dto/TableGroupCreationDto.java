package kitchenpos.order.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.ui.dto.request.TableGroupCreationRequest;

public class TableGroupCreationDto {

    private final List<OrderTableIdDto> orderTableIds;

    private TableGroupCreationDto(final List<OrderTableIdDto> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupCreationDto from(final TableGroupCreationRequest tableGroupCreationRequest) {
        final List<OrderTableIdDto> orderTableIdDtos = tableGroupCreationRequest.getOrderTables()
                .stream()
                .map(OrderTableIdDto::from)
                .collect(Collectors.toList());

        return new TableGroupCreationDto(orderTableIdDtos);
    }

    public List<OrderTableIdDto> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public String toString() {
        return "TableGroupCreationDto{" +
                "orderTableIds=" + orderTableIds +
                '}';
    }
}

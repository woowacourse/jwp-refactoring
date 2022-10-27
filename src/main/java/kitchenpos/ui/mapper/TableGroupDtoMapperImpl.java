package kitchenpos.ui.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.TableGroupCreateResponse;
import org.springframework.stereotype.Component;

@Component
public class TableGroupDtoMapperImpl implements TableGroupDtoMapper {

    @Override
    public TableGroupCreateResponse toTableGroupCreateResponse(final TableGroup tableGroup) {
        if (tableGroup == null) {
            return null;
        }

        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
                .stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());

        return new TableGroupCreateResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    private OrderTableResponse toOrderTableResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroup().getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}

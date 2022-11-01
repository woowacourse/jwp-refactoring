package kitchenpos.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.request.TableGroupRequest.TableId;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {

    private final OrderTableRepository orderTableRepository;

    public TableGroupMapper(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroup from(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(this::getOrderTable)
                .collect(Collectors.toList());
        // TODO: 최적화 필요

        return new TableGroup(savedOrderTables);
    }

    private OrderTable getOrderTable(final TableId tableId) {
        return orderTableRepository.findById(tableId.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}

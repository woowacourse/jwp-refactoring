package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.collection.OrderTables;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableServiceAssistant {

    private OrderTableRepository orderTableRepository;

    public TableServiceAssistant(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public OrderTables findTablesInGroup(Long tableGroupId) {
        return new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
    }

    public OrderTables findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(this::findTable)
                .collect(Collectors.toList());
        return new OrderTables(orderTables);
    }
}

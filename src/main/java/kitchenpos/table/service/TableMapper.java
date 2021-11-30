package kitchenpos.table.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {
    private final OrderTableRepository orderTableRepository;

    public TableMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable mapFrom(TableRequest tableRequest) {
        return new OrderTable(
                tableRequest.getNumberOfGuests(),
                tableRequest.getEmpty()
        );
    }

    public List<OrderTable> mapFrom(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(orderTableId -> findOrderTableById(orderTableId.getId()))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}

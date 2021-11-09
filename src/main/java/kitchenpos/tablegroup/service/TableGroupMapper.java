package kitchenpos.tablegroup.service;

import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.service.TableGroupRequest.OrderTableId;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {
    private final OrderTableRepository tableRepository;

    public TableGroupMapper(OrderTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableGroup mapFrom(TableGroupRequest tableGroupRequest) {
        return new TableGroup(
                tableGroupRequest.getOrderTables()
                        .stream().map(this::toOrderTable)
                        .collect(Collectors.toList())
        );
    }

    private OrderTable toOrderTable(OrderTableId orderTableId) {
        return tableRepository.findById(orderTableId.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}

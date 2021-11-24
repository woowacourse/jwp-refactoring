package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.domain.TableCreatable;
import org.springframework.stereotype.Component;

@Component
public class TableGroupCreatable implements TableCreatable {

    private final OrderTableRepository orderTableRepository;

    public TableGroupCreatable(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void create(Long tableGroupId, TableGroupRequest request) {
        final OrderTables orderTables = new OrderTables(orderTablesWith(request));
        final List<Long> orderTableIds = orderTableIdsWith(request);
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.checkValidity(orderTables);
        savedOrderTables.update(tableGroupId, false);

    }

    private List<OrderTable> orderTablesWith(TableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(it -> OrderTable.builder()
                        .id(it.getId())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Long> orderTableIdsWith(TableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}

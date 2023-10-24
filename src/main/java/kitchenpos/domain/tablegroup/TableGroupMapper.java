package kitchenpos.domain.tablegroup;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TableGroupMapper {

    private final OrderTableRepository orderTableRepository;

    public TableGroupMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroup toTableGroup(final CreateTableGroupRequest request) {
        return TableGroup.builder()
                .orderTables(getOrderTables(request.getOrderTables()))
                .createdDate(LocalDateTime.now())
                .build();
    }

    private List<OrderTable> getOrderTables(List<CreateTableGroupRequest.CreateOrderTable> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (CreateTableGroupRequest.CreateOrderTable createOrderTable : orderTableIds) {
            OrderTable entity = orderTableRepository.findById(createOrderTable.getId())
                    .orElseThrow(IllegalArgumentException::new);
            orderTables.add(entity);
        }
        return orderTables;
    }
}

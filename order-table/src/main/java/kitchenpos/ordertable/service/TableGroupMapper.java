package kitchenpos.ordertable.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {

    private final OrderTableService orderTableService;

    public TableGroupMapper(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    public TableGroup toEntity(TableGroupDto tableGroupDto) {
        List<OrderTable> orderTables = tableGroupDto.getOrderTables()
                                                    .stream()
                                                    .map(OrderTableDto::getId)
                                                    .map(orderTableService::getById)
                                                    .collect(toList());

        return new TableGroup.Builder()
            .createdDate(LocalDateTime.now())
            .orderTables(orderTables)
            .build();
    }
}

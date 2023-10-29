package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupOrderStatusValidator;
import kitchenpos.tablegroup.domain.TableGroupingService;
import org.springframework.stereotype.Component;

@Component
public class TableGroupingWithOrderTableService implements TableGroupingService {

    private final OrderTableRepository orderTableRepository;

    private final TableGroupOrderStatusValidator tableGroupOrderStatusValidator;

    public TableGroupingWithOrderTableService(final OrderTableRepository orderTableRepository,
                                              final TableGroupOrderStatusValidator tableGroupOrderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupOrderStatusValidator = tableGroupOrderStatusValidator;
    }

    @Override
    public void group(final List<Long> orderTableIds, final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    @Override
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(orderTable -> orderTable.ungroup(tableGroupOrderStatusValidator));
    }
}

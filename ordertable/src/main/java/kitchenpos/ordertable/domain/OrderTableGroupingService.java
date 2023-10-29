package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.GroupingService;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableGroupingService implements GroupingService {
    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupingService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void group(final List<Long> orderTableIds,
                      final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroupId);
        }
    }

    @Override
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            orderTable.detachFromGroup();
        }
    }
}

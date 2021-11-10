package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.response.TableGroupResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableRepository orderTableRepository,
                             OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        long orderTableCounts = orderTableRepository.countAllByIdIn(orderTableIds);

        if (orderTables.size() != orderTableCounts) {
            throw new IllegalArgumentException();
        }

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroup(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            orderTable.getOrders()
                .stream()
                .filter(Order::unableUngroup)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

            orderTable.ungroup();
        }
    }
}

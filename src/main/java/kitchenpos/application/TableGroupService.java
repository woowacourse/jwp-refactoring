package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
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

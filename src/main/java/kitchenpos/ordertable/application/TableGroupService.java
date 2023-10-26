package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.application.dto.TableGroupResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.repoisotory.OrderTableRepository;
import kitchenpos.ordertable.domain.repoisotory.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIdValues = request.getOrderTableIds();

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIdValues);

        if (orderTableIdValues.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedOrderTables.forEach(orderTable -> orderTable.group(savedTableGroup));

        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<OrderTable> groupTables = orderTableRepository.findAllByTableGroup(tableGroup);

        final List<Long> orderTableIds = groupTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTable_IdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : groupTables) {
            orderTable.deleteGroup();
        }
    }
}

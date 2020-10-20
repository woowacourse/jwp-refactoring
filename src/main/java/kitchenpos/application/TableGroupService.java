package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
//                throw new IllegalArgumentException();
//            }
//        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroup(savedTableGroup);
//            savedOrderTable.setEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public TableGroupResponse createWithRequest(final TableGroupRequest tableGroupRequest) {
        return null;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
//            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}

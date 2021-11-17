package kitchenpos.application;


import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
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

//        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        final List<OrderTable> savedOrderTables = new ArrayList<>();

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
//                throw new IllegalArgumentException();
//            }
//        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            savedOrderTable.setTableGroupId(tableGroupId);
//            savedOrderTable.setEmpty(false);
//            orderTableRepository.save(savedOrderTable);
//        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
//        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());

//        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable orderTable : orderTables) {
//            orderTable.setTableGroupId(null);
//            orderTable.setEmpty(false);
//            orderTableRepository.save(orderTable);
//        }
    }
}

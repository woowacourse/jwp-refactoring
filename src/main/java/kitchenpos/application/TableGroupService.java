//package kitchenpos.application;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import kitchenpos.domain.repository.OrderTableRepository;
//import kitchenpos.domain.repository.OrdersRepository;
//import kitchenpos.domain.repository.TableGroupRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//@Service
//@Transactional(readOnly = true)
//public class TableGroupService {
//
//    private final OrdersRepository ordersRepository;
//    private final OrderTableRepository orderTableRepository;
//    private final TableGroupRepository tableGroupRepository;
//
//    public TableGroupService(
//        final OrdersRepository ordersRepository,
//        final OrderTableRepository orderTableRepository,
//        final TableGroupRepository tableGroupRepository
//    ) {
//        this.ordersRepository = ordersRepository;
//        this.orderTableRepository = orderTableRepository;
//        this.tableGroupRepository = tableGroupRepository;
//    }
//
//    @Transactional
//    public TableGroup create(final TableGroup tableGroup) {
//        final List<OrderTable> orderTables = tableGroup.getOrderTables();
//
//        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
//            throw new IllegalArgumentException();
//        }
//
//        final List<Long> orderTableIds = orderTables.stream()
//            .map(OrderTable::getId)
//            .collect(Collectors.toList());
//
//        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
//
//        if (orderTables.size() != savedOrderTables.size()) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
//                throw new IllegalArgumentException();
//            }
//        }
//
//        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
//
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            orderTableRepository.save(savedOrderTable);
//        }
//        savedTableGroup.add(savedOrderTables);
//
//        return savedTableGroup;
//    }
//
//    @Transactional
//    public void ungroup(final Long tableGroupId) {
//        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
//            .orElseThrow(IllegalAccessError::new);
//
//        final List<OrderTable> orderTables = orderTableRepository
//            .findAllByTableGroupId(tableGroupId);
//
//        findTableGroup.remove(orderTables);
//
//        final List<Long> orderTableIds = orderTables.stream()
//            .map(OrderTable::getId)
//            .collect(Collectors.toList());
//
//        if (ordersRepository.existsByOrderTableIdInAndOrderStatusIn(
//            orderTableIds,
//            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
//        ) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable orderTable : orderTables) {
//            orderTableRepository.save(orderTable);
//        }
//    }
//}

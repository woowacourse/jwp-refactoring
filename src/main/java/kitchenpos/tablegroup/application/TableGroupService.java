package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.CannotUnGroupBecauseOfStatusException;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.exception.TableGroupInvalidSizeException;
import kitchenpos.tablegroup.exception.TableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest req) {
//        List<Long> orderTableIds = req.getOrderTables();
//
//        validateTableSize(orderTableIds);
//        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
//        validateTableNotFound(orderTableIds, savedOrderTables);
//
//        TableGroup tableGroup = TableGroup.createDefault();
//        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
//
//        for (final OrderTable orderTable : savedOrderTables) {
//            orderTable.initTableGroup(tableGroup);
//        }
//
//        return savedTableGroup;

        // 1. 주문 테이블이 2개보다 적으면 예외
        validateTableSize(req.getOrderTables());

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.createDefault());
        List<Long> orderTableIds = req.getOrderTables();

        List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        // 2. 테이블을 찾을 수 없다면 예외 발생
        validateTableNotFound(req.getOrderTables(), findOrderTables);

        findOrderTables.forEach(it ->
                it.updateTableGroupStatus(tableGroup.getId())
        );

        return TableGroupResponse.from(tableGroup, findOrderTables);
    }

    private void validateTableSize(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MINIMUM_TABLE_SIZE) {
            throw new TableGroupInvalidSizeException();
        }
    }

    private void validateTableNotFound(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new TableNotFoundException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupStatus(null);
        }
    }

    private void validateStatus(final List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (isStatusCookingOrMeal(orderTableIds)) {
            throw new CannotUnGroupBecauseOfStatusException();
        }
    }

    private boolean isStatusCookingOrMeal(final List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}

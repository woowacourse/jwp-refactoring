package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupService(
            final OrderRepository orderRepository,
            final TableGroupDao tableGroupDao,
            final OrderTableDao orderTableDao
    ) {
        this.orderRepository = orderRepository;
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getParsedOrderTableIds();
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateDuplicate(orderTableIds, orderTables);

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup())
                .group(orderTables);

        orderTableDao.updateAll(tableGroup.getOrderTables());

        return TableGroupResponse.of(tableGroup);
    }

    private void validateDuplicate(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderCompletion(orderTables);

        final List<OrderTable> ungroupedTables = orderTables.stream()
                .map(OrderTable::ungroup)
                .collect(Collectors.toList());

        orderTableDao.updateAll(ungroupedTables);
    }

    private void validateOrderCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}

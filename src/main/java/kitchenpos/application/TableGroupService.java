package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableDao orderTableDao;

    public TableGroupService(
            final OrderRepository orderRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderTableDao orderTableDao
    ) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getParsedOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateTableSize(orderTableIds, savedOrderTables);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private void validateTableSize(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateCompletion(orderTables);
        doUngroup(tableGroupId);
    }

    private void doUngroup(final Long tableGroupId) {
        for (final OrderTable orderTable : orderTableDao.findAllByTableGroupId(tableGroupId)) {
            orderTable.setTableGroupId(null);
            orderTable.changeEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private void validateCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}

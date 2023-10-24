package kitchenpos.application.tablegroup;

import kitchenpos.application.tablegroup.request.TableGroupCreateRequest;
import kitchenpos.domain.order.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        TableGroup tableGroup = mapToTableGroup(savedOrderTables, orderTableIds.size());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.changeStatus(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    private TableGroup mapToTableGroup(List<OrderTable> orderTables, int requestSize) {
        return TableGroup.of(orderTables, requestSize);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.changeStatus(false);
            orderTableRepository.save(orderTable);
        }
    }
}

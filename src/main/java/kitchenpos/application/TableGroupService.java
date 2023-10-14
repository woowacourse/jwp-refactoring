package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao,
                             TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableRequests().stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
        List<OrderTable> savedOrderTables = getOrderTables(orderTableIds);
        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(savedOrderTables));
        savedOrderTables.forEach(it -> it.group(savedTableGroup));
        return savedTableGroup;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException();
        }

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTables;
    }
}

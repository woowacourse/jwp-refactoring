package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(
        final OrderDao orderDao,
        final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
            .stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        validateOrderTablesCount(orderTableIds);

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateSavedTable(orderTableIds, savedOrderTables);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(savedOrderTables)
            .build();

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        orderTableDao.saveAll(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateOrderTablesCount(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSavedTable(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroup();

        orderTableDao.saveAll(orderTables);
        tableGroupDao.save(tableGroup);
    }
}

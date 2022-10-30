package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableGroupingSizeException;
import kitchenpos.exception.OrderTableUnableUngroupingStatusException;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupCreateRequest);
        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateOrderTablesSize(orderTableIds, savedOrderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        updateOrderTable(savedOrderTables, savedTableGroup.getId());

        return savedTableGroup;
    }

    private List<Long> getOrderTableIds(TableGroupCreateRequest tableGroupCreateRequest) {
        return tableGroupCreateRequest.getOrderTables().stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTablesSize(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new OrderTableGroupingSizeException();
        }
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new NotFoundOrderTableException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = getOrderTableIds(orderTables);

        validateOrderTablesStatus(orderTableIds);
        updateOrderTable(orderTables, null);
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void updateOrderTable(List<OrderTable> savedOrderTables, Long tableGroupId) {
        for (OrderTable savedOrderTable : savedOrderTables) {
            OrderTable orderTable = new OrderTable(savedOrderTable.getId(), tableGroupId,
                    savedOrderTable.getNumberOfGuests(), false);
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderTablesStatus(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderTableUnableUngroupingStatusException();
        }
    }
}

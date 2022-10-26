package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
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
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateNotExistTables(orderTableIds, savedOrderTables);

        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), savedOrderTables));
        group(savedOrderTables, tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private void validateNotExistTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블로 지정할 수 업습니다.");
        }
    }

    private void group(List<OrderTable> savedOrderTables, TableGroup tableGroup) {
        tableGroup.addOrderTables(savedOrderTables);
        for (OrderTable savedOrderTable : savedOrderTables) {
            orderTableDao.save(savedOrderTable);
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = extractTableIds(orderTables);
        validateNotCompletedOrderExist(orderTableIds);
        ungroup(orderTables);
    }

    private List<Long> extractTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateNotCompletedOrderExist(List<Long> orderTableIds) {
        boolean existNotCompletedOrder = orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        );
        if (existNotCompletedOrder) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있어서 해제할 수 없습니다.");
        }
    }

    private void ungroup(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}

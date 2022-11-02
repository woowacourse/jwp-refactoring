package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableGroupRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = mapToIds(tableGroupCreateRequest);
        final TableGroup tableGroup = TableGroup.of(orderTableDao.findAllByIdIn(orderTableIds));
        tableGroup.checkOrderTableSize(orderTableIds.size());
        return TableGroupResponse.from(tableGroupDao.save(tableGroup));
    }

    private static List<Long> mapToIds(final TableGroupCreateRequest tableGroupCreateRequest) {
        return tableGroupCreateRequest.getOrderTables()
                .stream()
                .map(OrderTableGroupRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable.changeEmptyTable());
        }
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                mapToOrderTableIds(orderTables), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 조리, 식사 상태면 해제할 수 없습니다.");
        }
    }

    private static List<Long> mapToOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

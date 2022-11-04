package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderDao;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableDao;
import kitchenpos.table.application.dto.TableGroupRequest;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.domain.repository.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupRequest request) {

        List<OrderTableRequest> orderTableRequests = request.getOrderTables();
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        OrderTables orderTables = new OrderTables(orderTableDao.findAllByIdIn(orderTableIds));
        orderTables.validateOrderTableSize(orderTableRequests.size());

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        savedTableGroup.addOrderTables(orderTables.mapToOrderTables(orderTableDao::save));
        return new TableGroupResponse(savedTableGroup);
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("[ERROR] 주문 상태가 COOKING 또는 MEAL일 때는 그룹을 해제할 수 없습니다.");
        }

        orderTables.forEach(it -> {
                    it.unGroup();
                    orderTableDao.save(it);
                });
    }
}

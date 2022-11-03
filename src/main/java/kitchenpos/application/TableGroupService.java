package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.Tables;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final Tables tables = new Tables(toOrderTables(tableGroupRequest));
        tables.validate();

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), tables));
        tableGroup.fillTables();

        updateAllTables(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> toOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId()))
                .collect(Collectors.toList());
    }

    private void updateAllTables(TableGroup tableGroup) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable table : tableGroup.getOrderTableValues()) {
            orderTables.add(orderTableDao.save(table));
        }
        tableGroup.placeOrderTables(new Tables(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupDao.findById(tableGroupId);
        Tables tables = tableGroup.getOrderTables();
        validateComplete(tables.getOrderTableIds());
        tables.ungroup();
        tableGroupDao.save(tableGroup);
    }

    private void validateComplete(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
        }
    }
}

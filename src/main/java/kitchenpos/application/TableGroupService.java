package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toTableGroup();

        final OrderTables orderTables =
            new OrderTables(orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds()));
        orderTables.connect(tableGroup);

        tableGroup.createWith(orderTables);
        tableGroupDao.save(tableGroup);
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables =
            new OrderTables(orderTableDao.findAllByTableGroupId(tableGroupId));
        validateCompletion(orderTables);
        orderTables.upGroupAll();
    }

    private void validateCompletion(OrderTables orderTables) {
        final List<Long> tableIds = orderTables.getIds();
        final List<OrderStatus> notCompleted = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(tableIds, notCompleted)) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.application.dto.TableGroupSaveRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.dao.OrderTableDao;
import kitchenpos.table.domain.dao.TableGroupDao;
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
    public TableGroupResponse create(TableGroupSaveRequest request) {
        List<Long> orderTableIds = request.toEntities();
        OrderTables orderTables = findOrderTables(orderTableIds);
        orderTables.validateSameSize(orderTableIds);

        TableGroup tableGroup = tableGroupDao.save(TableGroup.from());
        orderTables.joinWithTableGroup(tableGroup);
        saveOrderTables(orderTables);

        return TableGroupResponse.toResponse(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = findOrderTables(tableGroupId);
        orderTables.ungroup(orderDao.findAll());
       saveOrderTables(orderTables);
    }

    private void saveOrderTables(OrderTables orderTables) {
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            orderTableDao.save(orderTable);
        }
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        return new OrderTables(orderTableDao.findAllByIdIn(orderTableIds));
    }

    private OrderTables findOrderTables(Long tableGroupId) {
        return new OrderTables(orderTableDao.findAllByTableGroupId(tableGroupId));
    }
}

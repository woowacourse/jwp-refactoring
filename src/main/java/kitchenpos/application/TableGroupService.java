package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.application.exception.TableGroupNotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroup create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = new ArrayList<>();

        for (final CreateOrderGroupOrderTableRequest orderTableRequest : request.getOrderTables()) {
            final OrderTable findOrderTable = orderTableDao.findById(orderTableRequest.getId())
                                                           .orElseThrow(OrderTableNotFoundException::new);

            orderTables.add(findOrderTable);
        }

        final TableGroup tableGroup = new TableGroup(orderTables);

        return tableGroupDao.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                                                   .orElseThrow(TableGroupNotFoundException::new);

        tableGroup.ungroupOrderTables();
    }
}

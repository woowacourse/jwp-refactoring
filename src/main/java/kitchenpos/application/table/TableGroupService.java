package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
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
        tables.validateNoGroupAndEmpty();

        TableGroup entity = new TableGroup(LocalDateTime.now(), tables);
        TableGroup tableGroup = tableGroupDao.save(entity);
        tableGroup.fillTables();
        tableGroup.placeOrderTables(new Tables(orderTableDao.saveAll(tableGroup.getOrderTableValues())));
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> toOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupDao.findById(tableGroupId);
        Tables tables = tableGroup.getOrderTables();
        orderDao.validateComplete(tables.getOrderTableIds());
        tables.ungroup();
        tableGroupDao.save(tableGroup);
    }
}

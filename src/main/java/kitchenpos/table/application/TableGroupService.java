package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.request.TableGroupOrderTableRequest;
import kitchenpos.table.application.dto.request.TableGroupRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderStatusChangeValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.InvalidTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final OrderStatusChangeValidator orderStatusChangeValidator;

    public TableGroupService(OrderTableDao orderTableDao, TableGroupDao tableGroupDao,
                             OrderStatusChangeValidator orderStatusChangeValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderStatusChangeValidator = orderStatusChangeValidator;
    }

    public Long create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateTables(orderTableIds, orderTables);

        TableGroup tableGroup = TableGroup.create(LocalDateTime.now(), orderTables);
        Long tableGroupId = tableGroupDao.save(tableGroup);
        updateOrderTableEmpty(orderTables);

        return tableGroupId;
    }

    private void updateOrderTableEmpty(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTableDao.updateEmpty(orderTable.getId(), false);
        }
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables()
                .stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new InvalidTableException("테이블이 존재하지 않습니다.");
        }
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup(orderStatusChangeValidator);
            orderTableDao.updateTableGroupIdAndEmpty(orderTable.getId(), orderTable.getTableGroupId(),
                    orderTable.isEmpty());
        }
    }
}

package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.repository.dao.OrderTableDao;
import kitchenpos.table.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableDao orderTableDao;
    private final OrderValidator orderValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final OrderTableDao orderTableDao,
                             final OrderValidator orderValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableDao = orderTableDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroup tableGroup) {
        final List<Long> orderTableIds = mapToIds(tableGroup);
        final int orderTableCounts = orderTableDao.findAllByIdIn(orderTableIds)
                .size();
        if (orderTableIds.size() != orderTableCounts) {
            throw new IllegalArgumentException();
        }
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private static List<Long> mapToIds(final TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        orderValidator.validateUngroup(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable.changeEmptyTable());
        }
    }
}

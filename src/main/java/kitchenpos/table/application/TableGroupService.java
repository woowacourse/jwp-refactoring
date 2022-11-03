package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final OrderTables orderTables = new OrderTables(request.getOrderTables());
        final TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity());
        final OrderTables savedOrderTables = OrderTables.from(orderTableDao.findAllByIdIn(orderTables.getIds()), orderTables);

        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
                savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = getOrderTables(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(new OrderTable(orderTable.getId(), orderTable.getNumberOfGuests(), false));
        }
    }

    private List<OrderTable> getOrderTables(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderValidator.validateCompletion(orderTableIds);
        return orderTables;
    }
}

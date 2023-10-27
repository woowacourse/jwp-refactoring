package kitchenpos.tablegroup.application;

import kitchenpos.common.event.message.ValidatorHavingMeal;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.exception.TableGroupInvalidSizeException;
import kitchenpos.tablegroup.exception.TableNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository, final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest req) {
        validateCreateSize(req);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.createDefault());
        List<Long> orderTableIds = req.getOrderTables();

        List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateTableExist(orderTableIds, findOrderTables);
        updateTableGroupStatus(findOrderTables, tableGroup.getId());

        return TableGroupResponse.from(tableGroup, findOrderTables);
    }

    private void validateCreateSize(final TableGroupCreateRequest req) {
        if (CollectionUtils.isEmpty(req.getOrderTables()) || req.getOrderTables().size() < MINIMUM_TABLE_SIZE) {
            throw new TableGroupInvalidSizeException();
        }
    }

    private void validateTableExist(final List<Long> orderTableIds, final List<OrderTable> findOrderTables) {
        if (orderTableIds.size() != findOrderTables.size()) {
            throw new TableNotFoundException();
        }
    }

    private void updateTableGroupStatus(final List<OrderTable> findOrderTables, final Long tableGroupId) {
        findOrderTables.forEach(it -> it.updateTableGroupStatus(tableGroupId));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateStatusHavingMeal(orderTables);

        ungroupOrderTables(orderTables);
    }

    private void validateStatusHavingMeal(final List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        publisher.publishEvent(new ValidatorHavingMeal(orderTableIds));
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.updateTableGroupStatus(null));
    }
}

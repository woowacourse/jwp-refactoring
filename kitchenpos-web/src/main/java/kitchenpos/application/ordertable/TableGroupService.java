package kitchenpos.application.ordertable;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.application.ordertable.dto.OrderTableFindRequest;
import kitchenpos.application.ordertable.dto.TableGroupCreateRequest;
import kitchenpos.application.ordertable.dto.TableGroupResponse;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.TableGroup;
import kitchenpos.domain.orertable.TableGroupRepository;
import kitchenpos.domain.orertable.TableGroupValidator;
import org.springframework.stereotype.Service;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTableFindRequest> orderTableRequests = request.getOrderTables();
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableFindRequest::getId)
                .collect(Collectors.toUnmodifiableList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        tableGroupValidator.validateCreate(savedOrderTables, orderTableRequests.size(), savedOrderTables.size());
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        associateOrderTable(savedOrderTables, savedTableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void associateOrderTable(final List<OrderTable> savedOrderTables, final TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroup(savedTableGroup);
            orderTableRepository.save(savedOrderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateUnGroup(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }
}

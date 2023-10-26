package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.ordertable.application.dto.OrderTableFindRequest;
import kitchenpos.ordertable.application.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.application.dto.TableGroupResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.domain.TableGroupValidator;
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

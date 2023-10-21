package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupValidator;
import kitchenpos.dto.table.OrderTableFindRequest;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTableFindRequest> orderTableRequests = request.getOrderTables();
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableFindRequest::getId)
                .collect(Collectors.toUnmodifiableList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        TableGroupValidator.validateOrderTableSize(orderTableRequests.size(), savedOrderTables.size());

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        associateOrderTable(savedOrderTables, savedTableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void associateOrderTable(final List<OrderTable> savedOrderTables, final TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            TableGroupValidator.validateOrderTableStatus(savedOrderTable);
            savedOrderTable.confirmTableGroup(savedTableGroup);
            orderTableRepository.save(savedOrderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableGroupException.CannotUngroupStateByOrderStatusException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }
}

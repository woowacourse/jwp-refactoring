package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupsCreateRequest;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.NotCompletedOrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.exception.InvalidOrderTableToGroupException;
import kitchenpos.exception.TableGroupNotfoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupsCreateRequest tableGroupsCreateRequest) {
        final List<Long> orderTablesId = mapToOrderTableIds(tableGroupsCreateRequest);
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTablesId);
        validateDuplicatedOrderTableId(orderTablesId, orderTables);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    private static List<Long> mapToOrderTableIds(final TableGroupsCreateRequest tableGroupsCreateRequest) {
        return tableGroupsCreateRequest.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private static void validateDuplicatedOrderTableId(final List<Long> orderTablesId, final List<OrderTable> orderTables) {
        if (orderTablesId.size() != orderTables.size()) {
            throw new InvalidOrderTableToGroupException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotfoundException::new);
        validateAnyOrderTablesNotCompleted(tableGroup);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    private void validateAnyOrderTablesNotCompleted(final TableGroup tableGroup) {
        final List<Long> orderTableIds = extractOrderTableIds(tableGroup);
        try {
            orderValidator.validateAnyOrdersNotCompleted(orderTableIds);
        } catch (InvalidOrderStatusException e) {
            throw new NotCompletedOrderTableException();
        }
    }

    private List<Long> extractOrderTableIds(final TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

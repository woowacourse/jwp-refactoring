package kitchenpos.application.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ordertable.dto.OrderTableIdDto;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.application.tablegroup.dto.TableGroupResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.OrdersInTablesCompleteValidator;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrdersInTablesCompleteValidator ordersInTablesCompleteValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrdersInTablesCompleteValidator ordersInTablesCompleteValidator,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.ordersInTablesCompleteValidator = ordersInTablesCompleteValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = getOrderTablesWithIds(request.getOrderTables());
        validateOrderTablesAllExist(request, orderTables);
        validateAllOrderTablesCanBeGrouped(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroupCreatedAtNow());
        savedTableGroup.addOrderTables(orderTables);
        saveAllOrderTables(orderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> getOrderTablesWithIds(final List<OrderTableIdDto> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTablesAllExist(final TableGroupCreateRequest request,
                                             final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllOrderTablesCanBeGrouped(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableCanBeGrouped(orderTable);
        }
    }

    private void validateOrderTableCanBeGrouped(final OrderTable orderTable) {
        if (!orderTable.hasNoGroupAndEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup createTableGroupCreatedAtNow() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    private void saveAllOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTableRepository.save(orderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = findAllOrderTablesInGroup(tableGroupId);
        validateAllOrderInTableCompleted(orderTables);
        upGroupAllOrderTables(orderTables);
    }

    private List<OrderTable> findAllOrderTablesInGroup(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    private void validateAllOrderInTableCompleted(final List<OrderTable> orderTables) {
        final List<Long> orderIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        ordersInTablesCompleteValidator.validate(orderIds);
    }

    private void upGroupAllOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }
}

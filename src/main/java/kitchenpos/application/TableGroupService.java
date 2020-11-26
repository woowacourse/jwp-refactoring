package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.utils.OrderTablesFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTablesFactory orderTablesFactory;

    public TableGroupService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository,
        OrderTablesFactory orderTablesFactory) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTablesFactory = orderTablesFactory;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        OrderTables orderTables
            = new OrderTables(orderTablesFactory.generate(tableGroupCreateRequest));

        final List<OrderTable> savedOrderTables
            = orderTableRepository.findAllByIdIn(orderTables.extractIds());
        orderTables.validateWithSaved(savedOrderTables);

        final TableGroup savedTableGroup
            = saveTableGroup(tableGroupCreateRequest);
        saveNewOrderTable(savedOrderTables, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private TableGroup saveTableGroup(
        TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroup tableGroup
            = tableGroupCreateRequest.toEntity(LocalDateTime.now());
        return tableGroupRepository.save(tableGroup);
    }

    private void saveNewOrderTable(List<OrderTable> savedOrderTables,
        TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateOrderTable(savedOrderTable.getId(), savedTableGroup,
                savedOrderTable.getNumberOfGuests(), false);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 요리, 식사인 경우 해제할 수 없습니다.");
        }

        saveChangedOrderTable(orderTables);
    }

    private void saveChangedOrderTable(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateOrderTable(orderTable.getId(), null,
                orderTable.getNumberOfGuests(), false);
        }
    }
}

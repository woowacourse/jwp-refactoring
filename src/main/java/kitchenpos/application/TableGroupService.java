package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.TableGroupCreateRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.mapper.TableGroupMapper;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(
            final TableGroupCreateRequest tableGroupCreateRequest
    ) {
        final List<Long> orderTableIds = tableGroupCreateRequest.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableSize(orderTables, orderTableIds);
        validateNumberOfOrderTable(orderTables);
        validateOrderTableStatus(orderTables);

        final TableGroup tableGroup = TableGroupMapper.toTableGroup(LocalDateTime.now(), orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(savedTableGroup);
        }

        return TableGroupMapper.toTableGroupResponse(savedTableGroup);
    }

    private void validateOrderTableStatus(
            final List<OrderTable> savedOrderTables
    ) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
            }
        }
    }

    private void validateOrderTableSize(
            final List<OrderTable> orderTables,
            final List<Long> orderTableIds
    ) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new NoSuchElementException("존재하지 order table 입니다.");
        }
    }

    private void validateNumberOfOrderTable(
            final List<OrderTable> orderTables
    ) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("order table 수는 2 이상이어야 합니다.");
        }
    }

    public void ungroup(
            final Long tableGroupId
    ) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 table group 입니다."));
        validateOrderStatus(tableGroup);
        tableGroup.ungroup();
    }

    private void validateOrderStatus(
            final TableGroup tableGroup
    ) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                tableGroup.getOrderTables(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 MEAL, COOKING 이면 그룹을 해제할 수 없습니다.");
        }
    }
}

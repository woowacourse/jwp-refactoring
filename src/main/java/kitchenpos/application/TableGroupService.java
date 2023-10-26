package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

@Service
@Transactional
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

    public Long create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.extractIds());
        validateAllTableGroupFound(request, orderTables);
        final TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), orderTables);
        final TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return saveTableGroup.getId();
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final OrderTables orderTables = tableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderTableStatus(orderTableIds);
        orderTables.reset();
    }

    private void validateOrderTableStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllTableGroupFound(final TableGroupCreateRequest request, final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableId;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.*;

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
        validateTableSize(request);
        final List<Long> orderTableIds = extractIds(request);
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateAllFound(request, savedOrderTables);
        final TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), savedOrderTables);
        return tableGroupRepository.save(tableGroup).getId();
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new).getOrderTables();
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderTableStatus(orderTableIds);
        orderTables.reset();
    }

    private static List<Long> extractIds(final TableGroupCreateRequest request) {
        return request.getOrderTables().stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTableStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllFound(final TableGroupCreateRequest request, final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateTableSize(final TableGroupCreateRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderTables()) || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException();
        }
    }
}

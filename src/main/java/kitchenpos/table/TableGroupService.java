package kitchenpos.table;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.JpaOrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderTableRepository jpaOrderTableRepository;
    private final JpaTableGroupRepository jpaTableGroupRepository;

    public TableGroupService(
            final JpaOrderRepository jpaOrderRepository,
            final JpaOrderTableRepository jpaOrderTableRepository,
            final JpaTableGroupRepository jpaTableGroupRepository
    ) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaOrderTableRepository = jpaOrderTableRepository;
        this.jpaTableGroupRepository = jpaTableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {

        List<Long> orderTableIds = request.getOrderTableIds().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = jpaOrderTableRepository.findAllByIdIn(orderTableIds);

        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = jpaTableGroupRepository.save(tableGroup);
        savedTableGroup.setOrderTables(savedOrderTables);
        return new TableGroupResponse(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = jpaTableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (jpaOrderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroup();
    }
}

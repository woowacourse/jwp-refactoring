package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.dao.JpaTableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
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
            throw new IllegalArgumentException("");
        }

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(savedOrderTables);

        final TableGroup savedTableGroup = jpaTableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroup(savedTableGroup);
            savedOrderTable.setEmpty(false);
            jpaOrderTableRepository.save(savedOrderTable);
        }

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

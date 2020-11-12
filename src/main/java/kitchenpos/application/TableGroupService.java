package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TableGroupService {
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables orderTables = orderService.findAllByIdIn(tableGroup.extractOrderTableIds());
        if (orderTables.getOrderTables().stream()
            .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new IllegalArgumentException();
        }
        orderTables.changeStatus();
        tableGroup.modifyOrderTables(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = orderService.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.extractIds();
        if (orderService.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderService.ungroupTables(orderTables);
    }
}

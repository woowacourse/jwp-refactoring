package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TableGroupService {
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables orderTables = orderService.findAllByIdIn(tableGroup.extractOrderTableIds());
        orderTables.changeEmptyStatus();
        tableGroup.modifyOrderTables(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        final List<Long> orderTableIds = tableGroup.extractOrderTableIds();
        if (orderService.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroupTables();
    }
}

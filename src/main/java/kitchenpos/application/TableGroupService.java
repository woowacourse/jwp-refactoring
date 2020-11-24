package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

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

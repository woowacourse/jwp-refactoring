package kitchenpos.ordertable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.JpaOrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.TableGrouping;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderTableGrouping implements TableGrouping {

    private final JpaOrderTableRepository jpaOrderTableRepository;
    private final JpaOrderRepository jpaOrderRepository;

    public OrderTableGrouping(
            final JpaOrderTableRepository jpaOrderTableRepository,
            final JpaOrderRepository jpaOrderRepository
    ) {
        this.jpaOrderTableRepository = jpaOrderTableRepository;
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void group(TableGroup tableGroup, List<Long> orderTableIds) {

        final List<OrderTable> savedOrderTables = jpaOrderTableRepository.findAllByIdIn(orderTableIds);

        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }

        if (CollectionUtils.isEmpty(savedOrderTables) || savedOrderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (savedOrderTable == null) {
                throw new IllegalArgumentException();
            }

            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }

            savedOrderTable.setTableGroupId(tableGroup.getId());
            savedOrderTable.changeEmpty(false);
        }
    }

    @Override
    public void ungroup(TableGroup tableGroup) {

        List<OrderTable> savedOrderTables = jpaOrderTableRepository.findAllByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = savedOrderTables.stream().map(OrderTable::getId).collect(Collectors.toList());

        if (jpaOrderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.ungroup();
        }
    }

}

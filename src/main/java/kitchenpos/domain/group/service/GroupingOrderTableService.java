package kitchenpos.domain.group.service;

import java.util.List;
import kitchenpos.config.DomainComponent;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.domain.exception.InvalidOrderTableStatusException;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.service.GroupingTableService;
import org.springframework.util.CollectionUtils;

@DomainComponent
public class GroupingOrderTableService implements GroupingTableService {

    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public GroupingOrderTableService(
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void group(final List<Long> orderTableIds, final TableGroup tableGroup) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MINIMUM_TABLE_SIZE) {
            throw new InvalidOrderTableSizeException();
        }

        for (final Long orderTableId : orderTableIds) {
            final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                              .orElseThrow(InvalidOrderTableException::new);

            grouping(orderTable, tableGroup);
        }
    }

    private void grouping(final OrderTable orderTable, final TableGroup tableGroup) {
        if (!orderTable.isEmpty()) {
            throw new InvalidEmptyOrderTableException();
        }

        orderTable.group(tableGroup.getId());
    }

    @Override
    public void ungroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        for (final OrderTable orderTable : orderTables) {
            final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

            validateOrderStatus(orders);

            orderTable.ungroup();
        }
    }

    private void validateOrderStatus(final List<Order> orders) {
        for (final Order order : orders) {
            if (!order.isCompletion()) {
                throw new InvalidOrderTableStatusException();
            }
        }
    }
}

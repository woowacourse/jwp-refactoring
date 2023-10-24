package kitchenpos.table_group.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.OrderTables;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public TableGroup create(final TableGroup tableGroup) {
    final List<Long> orderTableIds = tableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());

    final OrderTables savedOrderTables = new OrderTables(
        orderTableRepository.findAllByIdIn(orderTableIds)
    );

    savedOrderTables.validateMatchingOrderTableSize(orderTableIds.size());
    savedOrderTables.validateEmptyOrBelongTableGroup();

    return tableGroupRepository.save(
        new TableGroup(
            LocalDateTime.now(),
            savedOrderTables.getOrderTables()
        )
    );
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final OrderTables savedOrderTables = new OrderTables(
        orderTableRepository.findAllByTableGroupId(tableGroupId)
    );

    final List<Long> orderTableIds = savedOrderTables.getOrderTableIds();

    validateOrderTableCompletion(orderTableIds);

    final List<OrderTable> ungroupingOrderTables = savedOrderTables.ungrouping();
    for (OrderTable ungroupingOrderTable : ungroupingOrderTables) {
      orderTableRepository.save(ungroupingOrderTable);
    }
  }

  private void validateOrderTableCompletion(final List<Long> orderTableIds) {
    if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, OrderStatus.notCompletion())
    ) {
      throw new IllegalArgumentException();
    }
  }
}

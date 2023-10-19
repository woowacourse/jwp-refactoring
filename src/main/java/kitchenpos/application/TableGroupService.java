package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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
    savedOrderTables.validateNotEmptyOrNotBelongTableGroup();

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

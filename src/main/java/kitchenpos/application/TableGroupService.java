package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup2;
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
  public TableGroup2 create(final TableGroup2 tableGroup) {
    final OrderTables orderTables = new OrderTables(tableGroup.getOrderTables());

    final List<Long> orderTableIds = orderTables.getOrderTableIds();

    final OrderTables savedOrderTables = new OrderTables(
        orderTableRepository.findAllByIdIn(orderTableIds)
    );

    if (orderTables.isNotSameSize(savedOrderTables)) {
      throw new IllegalArgumentException();
    }

    if (savedOrderTables.isNotEmptyOrNotBelongTableGroup()) {
      throw new IllegalArgumentException();
    }

    return tableGroupRepository.save(
        new TableGroup2(
            LocalDateTime.now(),
            savedOrderTables.getOrderTables()
        )
    );
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final OrderTables orderTables = new OrderTables(
        orderTableRepository.findAllByTableGroupId(tableGroupId)
    );

    final List<Long> orderTableIds = orderTables.getOrderTableIds();

    if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
      throw new IllegalArgumentException();
    }

    final List<OrderTable2> ungroupingOrderTables = orderTables.ungrouping();

    for (OrderTable2 ungroupingOrderTable : ungroupingOrderTables) {
      orderTableRepository.save(ungroupingOrderTable);
    }
  }
}

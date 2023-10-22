package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.OrderTableQueryResponse;
import kitchenpos.table.application.dto.OrderTableReferenceRequest;
import kitchenpos.table.application.dto.TableGroupCreateRequest;
import kitchenpos.table.application.dto.TableGroupQueryResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

  private static final int MIN_TABLE_GROUP_SIZE = 2;
  private final OrderRepository orderRepository;
  private final OrderTableRepository orderTableRepository;
  private final TableGroupRepository tableGroupRepository;

  public TableGroupService(final OrderRepository orderRepository,
      final OrderTableRepository orderTableRepository,
      final TableGroupRepository tableGroupRepository) {
    this.orderRepository = orderRepository;
    this.orderTableRepository = orderTableRepository;
    this.tableGroupRepository = tableGroupRepository;
  }

  @Transactional
  public TableGroupQueryResponse create(final TableGroupCreateRequest request) {
    final List<Long> orderTableIds =
        request.getOrderTables()
            .stream()
            .map(OrderTableReferenceRequest::getId)
            .collect(Collectors.toList());
    final OrderTables savedOrderTables = new OrderTables(
        orderTableRepository.findAllByIdIn(orderTableIds));
    validateTableGroupCreateRequest(savedOrderTables, orderTableIds);

    final TableGroup savedTableGroup = tableGroupRepository.save(
        request.toTableGroup(savedOrderTables));

    final List<OrderTableQueryResponse> orderTableQueryResponses =
        OrderTableQueryResponse.from(savedTableGroup.getOrderTables());
    return new TableGroupQueryResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
        orderTableQueryResponses);
  }

  private void validateTableGroupCreateRequest(final OrderTables savedOrderTables,
      final List<Long> orderTableIds) {
    validateOrderTablesSize(savedOrderTables);
    validateAllOrderTablesExist(savedOrderTables, orderTableIds);
    savedOrderTables.validateAllOrderTablesEmptyAndNotHaveTableGroup();
  }

  private void validateOrderTablesSize(final OrderTables savedOrderTables) {
    if (savedOrderTables.isEmpty() || savedOrderTables.size() < MIN_TABLE_GROUP_SIZE) {
      throw new IllegalArgumentException();
    }
  }

  private void validateAllOrderTablesExist(final OrderTables savedOrderTables,
      final List<Long> orderTableIds) {
    if (savedOrderTables.isDifferentSize(orderTableIds.size())) {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final OrderTables orderTables = new OrderTables(
        orderTableRepository.findAllByTableGroupId(tableGroupId));

    final List<Long> orderTableIds = orderTables.extractOrderTableIds();

    validateAllOrderTablesCompletion(orderTableIds);

    final List<OrderTable> ungroupedTables = orderTables.ungroup();
    for (final OrderTable orderTable : ungroupedTables) {
      orderTableRepository.save(orderTable);
    }
  }

  private void validateAllOrderTablesCompletion(final List<Long> orderTableIds) {
    if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, OrderStatus.NOT_COMPLETION_STATUSES)) {
      throw new IllegalArgumentException();
    }
  }
}

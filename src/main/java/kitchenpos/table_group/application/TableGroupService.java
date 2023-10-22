package kitchenpos.table_group.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.application.dto.OrderTableQueryResponse;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import kitchenpos.table_group.application.dto.OrderTableCreateRequest;
import kitchenpos.table_group.application.dto.TableGroupCreateRequest;
import kitchenpos.table_group.application.dto.TableGroupQueryResponse;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    final List<OrderTableCreateRequest> orderTables = request.getOrderTables();

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTableCreateRequest::getId)
        .collect(Collectors.toList());

    final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

    if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_GROUP_SIZE) {
      throw new IllegalArgumentException();
    }
    if (orderTables.size() != savedOrderTables.size()) {
      throw new IllegalArgumentException();
    }
    for (final OrderTable savedOrderTable : savedOrderTables) {
      if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
        throw new IllegalArgumentException();
      }
    }

    final TableGroup tableGroup = request.toTableGroup(savedOrderTables);

    final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

    final List<OrderTableQueryResponse> orderTableQueryResponses =
        savedTableGroup.getOrderTables()
            .stream()
            .map(OrderTableQueryResponse::from)
            .collect(Collectors.toList());
    return new TableGroupQueryResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
        orderTableQueryResponses);
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());

    if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, OrderStatus.NOT_COMPLETION_STATUSES)) {
      throw new IllegalArgumentException();
    }

    for (final OrderTable orderTable : orderTables) {
      orderTableRepository.save(
          new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false));
    }
  }
}

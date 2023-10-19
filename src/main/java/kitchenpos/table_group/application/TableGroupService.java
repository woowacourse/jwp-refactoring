package kitchenpos.table_group.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order_table.application.dto.OrderTableQueryResponse;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import kitchenpos.table_group.application.dto.OrderTableCreateRequest;
import kitchenpos.table_group.application.dto.TableGroupCreateRequest;
import kitchenpos.table_group.application.dto.TableGroupQueryResponse;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.persistence.TableGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

  private final OrderDao orderDao;
  private final OrderTableDao orderTableDao;
  private final TableGroupDao tableGroupDao;

  public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
      final TableGroupDao tableGroupDao) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
  }

  @Transactional
  public TableGroupQueryResponse create(final TableGroupCreateRequest tableGroup) {
    final List<OrderTableCreateRequest> orderTables = tableGroup.getOrderTables();

    if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
      throw new IllegalArgumentException();
    }

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTableCreateRequest::getId)
        .collect(Collectors.toList());

    final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

    if (orderTables.size() != savedOrderTables.size()) {
      throw new IllegalArgumentException();
    }

    for (final OrderTable savedOrderTable : savedOrderTables) {
      if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
        throw new IllegalArgumentException();
      }
    }

    final TableGroup savedTableGroup = tableGroupDao.save(
        new TableGroup(LocalDateTime.now()));
    final Long tableGroupId = savedTableGroup.getId();

    final List<OrderTable> orderTables2 = savedOrderTables.stream()
        .map(savedOrderTable -> new OrderTable(
            savedOrderTable.getId(),
            tableGroupId,
            savedOrderTable.getNumberOfGuests(),
            false
        ))
        .collect(Collectors.toList());

    for (final OrderTable savedOrderTable : orderTables2) {
      orderTableDao.save(savedOrderTable);
    }
    final List<OrderTableQueryResponse> orderTableQueryResponses =
        orderTables2.stream()
            .map(OrderTableQueryResponse::from)
            .collect(Collectors.toList());
    return new TableGroupQueryResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
        orderTableQueryResponses);
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());

    if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
      throw new IllegalArgumentException();
    }

    for (final OrderTable orderTable : orderTables) {
      orderTableDao.save(
          new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false));
    }
  }
}

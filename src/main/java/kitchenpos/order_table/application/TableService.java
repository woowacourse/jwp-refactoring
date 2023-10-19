package kitchenpos.order_table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order_table.application.dto.OrderTableCreateRequest;
import kitchenpos.order_table.application.dto.OrderTableEmptyModifyRequest;
import kitchenpos.order_table.application.dto.OrderTableNumberOfGuestModifyRequest;
import kitchenpos.order_table.application.dto.OrderTableQueryResponse;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

  private final OrderDao orderDao;
  private final OrderTableDao orderTableDao;

  public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
  }

  @Transactional
  public OrderTableQueryResponse create(final OrderTableCreateRequest request) {
    final OrderTable savedOrderTable = request.toOrderTable();

    return OrderTableQueryResponse.from(orderTableDao.save(savedOrderTable));
  }

  public List<OrderTableQueryResponse> list() {
    return orderTableDao.findAll()
        .stream()
        .map(OrderTableQueryResponse::from)
        .collect(Collectors.toList());
  }

  @Transactional
  public OrderTableQueryResponse changeEmpty(final Long orderTableId,
      final OrderTableEmptyModifyRequest orderTable) {
    final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
      throw new IllegalArgumentException();
    }

    if (orderDao.existsByOrderTableIdAndOrderStatusIn(
        orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
      throw new IllegalArgumentException();
    }

    return OrderTableQueryResponse.from(orderTableDao.save(new OrderTable(savedOrderTable.getId(),
        savedOrderTable.getTableGroupId(), savedOrderTable.getNumberOfGuests(),
        orderTable.isEmpty())));
  }

  @Transactional
  public OrderTableQueryResponse changeNumberOfGuests(final Long orderTableId,
      final OrderTableNumberOfGuestModifyRequest orderTable) {
    final int numberOfGuests = orderTable.getNumberOfGuests();

    if (numberOfGuests < 0) {
      throw new IllegalArgumentException();
    }

    final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    if (savedOrderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }

    return OrderTableQueryResponse.from(orderTableDao.save(new OrderTable(savedOrderTable.getId(),
        savedOrderTable.getTableGroupId(), numberOfGuests, savedOrderTable.isEmpty())));
  }
}

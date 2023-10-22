package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableEmptyModifyRequest;
import kitchenpos.table.application.dto.OrderTableNumberOfGuestModifyRequest;
import kitchenpos.table.application.dto.OrderTableQueryResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

  private static final int MIN_NUMBER_OF_GUESTS = 0;
  private final OrderRepository orderRepository;
  private final OrderTableRepository orderTableDao;

  public TableService(final OrderRepository orderRepository,
      final OrderTableRepository orderTableDao) {
    this.orderRepository = orderRepository;
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
      final OrderTableEmptyModifyRequest request) {
    final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);
    validateHasNotTableGroup(savedOrderTable);
    validateAllOrdersCompletion(orderTableId);

    savedOrderTable.updateEmpty(request.isEmpty());

    return OrderTableQueryResponse.from(orderTableDao.save(savedOrderTable));
  }

  private void validateHasNotTableGroup(final OrderTable savedOrderTable) {
    if (savedOrderTable.hasTableGroup()) {
      throw new IllegalArgumentException();
    }
  }

  private void validateAllOrdersCompletion(final Long orderTableId) {
    if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
        OrderStatus.NOT_COMPLETION_STATUSES)) {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public OrderTableQueryResponse changeNumberOfGuests(final Long orderTableId,
      final OrderTableNumberOfGuestModifyRequest request) {
    final int numberOfGuests = request.getNumberOfGuests();
    final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);
    validateNumberOfGuests(numberOfGuests);
    validateNotEmpty(savedOrderTable);

    savedOrderTable.updateNumberOfGuests(numberOfGuests);

    return OrderTableQueryResponse.from(orderTableDao.save(savedOrderTable));
  }

  private static void validateNotEmpty(final OrderTable savedOrderTable) {
    if (savedOrderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  private void validateNumberOfGuests(final int numberOfGuests) {
    if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
      throw new IllegalArgumentException();
    }
  }
}

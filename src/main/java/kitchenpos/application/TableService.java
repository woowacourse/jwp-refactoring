package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

  private final OrderTableRepository orderTableRepository;
  private final OrderRepository orderRepository;

  public TableService(
      final OrderTableRepository orderTableRepository,
      final OrderRepository orderRepository
  ) {
    this.orderTableRepository = orderTableRepository;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public OrderTable create(final OrderTable orderTable) {
    return orderTableRepository.save(orderTable);
  }

  public List<OrderTable> list() {
    return orderTableRepository.findAll();
  }

  @Transactional
  public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
    final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    savedOrderTable.validateNotBelongTableGroup();
    validateOrderTableNotCompletion(orderTableId);

    savedOrderTable.changeEmpty(orderTable.isEmpty());

    return orderTableRepository.save(savedOrderTable);
  }

  private void validateOrderTableNotCompletion(final Long orderTableId) {
    if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
        orderTableId, OrderStatus.notCompletion())
    ) {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
    final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    savedOrderTable.validateEmpty();

    savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

    return orderTableRepository.save(savedOrderTable);
  }
}

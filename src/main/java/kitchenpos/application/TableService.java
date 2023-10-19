package kitchenpos.application;

import java.util.Arrays;
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

    if (savedOrderTable.isNotBelongTableGroup()) {
      throw new IllegalArgumentException();
    }

    if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
        orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
      throw new IllegalArgumentException();
    }

    savedOrderTable.changeEmpty(orderTable.isEmpty());

    return orderTableRepository.save(savedOrderTable);
  }

  @Transactional
  public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
    final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    if (savedOrderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }

    savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

    return orderTableRepository.save(savedOrderTable);
  }
}

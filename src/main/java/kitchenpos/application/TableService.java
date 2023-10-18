package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
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
  public OrderTable2 create(final OrderTable2 orderTable) {
    return orderTableRepository.save(orderTable);
  }

  public List<OrderTable2> list() {
    return orderTableRepository.findAll();
  }

  @Transactional
  public OrderTable2 changeEmpty(final Long orderTableId, final OrderTable2 orderTable) {
    final OrderTable2 savedOrderTable = orderTableRepository.findById(orderTableId)
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
  public OrderTable2 changeNumberOfGuests(final Long orderTableId, final OrderTable2 orderTable) {
    final OrderTable2 savedOrderTable = orderTableRepository.findById(orderTableId)
        .orElseThrow(IllegalArgumentException::new);

    if (savedOrderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }

    savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

    return orderTableRepository.save(savedOrderTable);
  }
}

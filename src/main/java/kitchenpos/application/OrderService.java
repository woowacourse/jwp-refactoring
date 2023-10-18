package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final MenuRepository menuRepository;
  private final OrderRepository orderRepository;
  private final OrderTableRepository orderTableRepository;

  public OrderService(
      final MenuRepository menuRepository,
      final OrderRepository orderRepository,
      final OrderTableRepository orderTableRepository
  ) {
    this.menuRepository = menuRepository;
    this.orderRepository = orderRepository;
    this.orderTableRepository = orderTableRepository;
  }

  @Transactional
  public Order2 create(final Order2 order) {
    final OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItems());

    final List<Long> menuIds = orderLineItems.getMenuIds();

    if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
      throw new IllegalArgumentException();
    }

    final OrderTable2 orderTable = orderTableRepository.findById(order.getOrderTable().getId())
        .orElseThrow(IllegalArgumentException::new);

    if (orderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }

    final Order2 saveOrder = new Order2(
        orderTable,
        OrderStatus.COOKING,
        LocalDateTime.now(),
        order.getOrderLineItems()
    );

    return orderRepository.save(saveOrder);
  }

  public List<Order2> list() {
    return orderRepository.findAll();
  }

  @Transactional
  public Order2 changeOrderStatus(final Long orderId, final Order2 order) {
    final Order2 savedOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalArgumentException::new);

    if (savedOrder.isCompletion()) {
      throw new IllegalArgumentException();
    }

    savedOrder.changeStatus(order.getOrderStatus());

    return orderRepository.save(savedOrder);
  }
}

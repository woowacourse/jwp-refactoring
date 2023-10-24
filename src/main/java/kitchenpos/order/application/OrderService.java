package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderStatusModifyRequest;
import kitchenpos.order.application.dto.response.OrderQueryResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
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
  public OrderQueryResponse create(final OrderCreateRequest request) {
    final Order orderRequest = request.toOrder();
    final OrderLineItems orderLineItemRequests = orderRequest.getOrderLineItems();
    final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
        .orElseThrow(IllegalArgumentException::new);
    final List<Long> menuIds = orderLineItemRequests.extractMenuIds();

    validateOrderCreateRequest(orderLineItemRequests, menuIds, orderTable);

    final Order order = new Order(orderTable.getId(), OrderStatus.COOKING,
        LocalDateTime.now(), orderLineItemRequests);

    return OrderQueryResponse.from(orderRepository.save(order));
  }

  private void validateOrderCreateRequest(
      final OrderLineItems orderLineItems,
      final List<Long> menuIds, final OrderTable orderTable) {
    validateOrderLineItemsNotEmpty(orderLineItems);
    validateMenus(orderLineItems, menuIds);
    validateOrderTableNotEmpty(orderTable);
  }

  private void validateOrderLineItemsNotEmpty(final OrderLineItems orderLineItems) {
    if (orderLineItems.isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  private void validateMenus(final OrderLineItems orderLineItems, final List<Long> menuIds) {
    if (orderLineItems.isDifferentSize(menuRepository.countByIdIn(menuIds))) {
      throw new IllegalArgumentException();
    }
  }

  private void validateOrderTableNotEmpty(final OrderTable orderTable) {
    if (orderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  public List<OrderQueryResponse> list() {
    return orderRepository.findAll()
        .stream()
        .map(OrderQueryResponse::from)
        .collect(Collectors.toList());
  }

  @Transactional
  public OrderQueryResponse changeOrderStatus(final Long orderId,
      final OrderStatusModifyRequest request) {
    final Order savedOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalArgumentException::new);
    final OrderStatus orderStatus = request.getOrderStatus();
    validateNotCompletion(savedOrder.getOrderStatus());

    savedOrder.updateOrderStatus(orderStatus);

    return OrderQueryResponse.from(orderRepository.save(savedOrder));
  }

  private void validateNotCompletion(final OrderStatus orderStatus) {
    if (OrderStatus.COMPLETION.equals(orderStatus)) {
      throw new IllegalArgumentException();
    }
  }
}

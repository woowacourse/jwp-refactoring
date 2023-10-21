package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.persistence.MenuRepositoryImpl;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderQueryResponse;
import kitchenpos.order.application.dto.OrderStatusModifyRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

  private final MenuRepository menuRepository;
  private final OrderRepository orderRepository;
  private final OrderTableDao orderTableDao;

  public OrderService(
      final MenuRepositoryImpl menuRepository,
      final OrderRepository orderRepository,
      final OrderTableDao orderTableDao
  ) {
    this.menuRepository = menuRepository;
    this.orderRepository = orderRepository;
    this.orderTableDao = orderTableDao;
  }

  @Transactional
  public OrderQueryResponse create(final OrderCreateRequest request) {
    final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();

    final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
        .orElseThrow(IllegalArgumentException::new);

    final List<Long> menuIds = orderLineItemRequests.stream()
        .map(OrderLineItemCreateRequest::getMenuId)
        .collect(Collectors.toList());

    validateOrderRequest(orderLineItemRequests, menuIds, orderTable);

    final List<OrderLineItem> orderLineItems =
        orderLineItemRequests.stream()
            .map(OrderLineItemCreateRequest::toOrderLineItem)
            .collect(Collectors.toList());
    final Order order = new Order(orderTable.getId(), OrderStatus.COOKING,
        LocalDateTime.now(), orderLineItems);

    return OrderQueryResponse.from(orderRepository.save(order));
  }

  private void validateOrderRequest(final List<OrderLineItemCreateRequest> orderLineItemRequests,
      final List<Long> menuIds, final OrderTable orderTable) {
    validateOrderLineItemsNotEmpty(orderLineItemRequests);
    validateMenus(orderLineItemRequests, menuIds);
    validateOrderTableNotEmpty(orderTable);
  }

  private void validateOrderLineItemsNotEmpty(
      final List<OrderLineItemCreateRequest> orderLineItems) {
    if (CollectionUtils.isEmpty(orderLineItems)) {
      throw new IllegalArgumentException();
    }
  }

  private void validateMenus(final List<OrderLineItemCreateRequest> orderLineItems,
      final List<Long> menuIds) {
    if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
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
    if (OrderStatus.COMPLETION.isEqual(orderStatus)) {
      throw new IllegalArgumentException();
    }
  }
}

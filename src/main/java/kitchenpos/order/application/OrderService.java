package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemQueryResponse;
import kitchenpos.order.application.dto.OrderQueryResponse;
import kitchenpos.order.application.dto.OrderStatusModifyRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order.persistence.OrderLineItemDao;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

  private final MenuDao menuDao;
  private final OrderDao orderDao;
  private final OrderLineItemDao orderLineItemDao;
  private final OrderTableDao orderTableDao;

  public OrderService(
      final MenuDao menuDao,
      final OrderDao orderDao,
      final OrderLineItemDao orderLineItemDao,
      final OrderTableDao orderTableDao
  ) {
    this.menuDao = menuDao;
    this.orderDao = orderDao;
    this.orderLineItemDao = orderLineItemDao;
    this.orderTableDao = orderTableDao;
  }

  @Transactional
  public OrderQueryResponse create(final OrderCreateRequest order) {
    final List<OrderLineItemCreateRequest> orderLineItems = order.getOrderLineItems();

    if (CollectionUtils.isEmpty(orderLineItems)) {
      throw new IllegalArgumentException();
    }

    final List<Long> menuIds = orderLineItems.stream()
        .map(OrderLineItemCreateRequest::getMenuId)
        .collect(Collectors.toList());

    if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
      throw new IllegalArgumentException();
    }

    final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
        .orElseThrow(IllegalArgumentException::new);

    if (orderTable.isEmpty()) {
      throw new IllegalArgumentException();
    }

    final Order savedOrder = orderDao.save(
        new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

    final Long orderId = savedOrder.getId();
    final List<OrderLineItemQueryResponse> savedOrderLineItems = new ArrayList<>();
    for (final OrderLineItemCreateRequest orderLineItem : orderLineItems) {
      savedOrderLineItems.add(OrderLineItemQueryResponse.from(orderLineItemDao.save(
          new OrderLineItem(orderId, orderLineItem.getMenuId(),
              orderLineItem.getQuantity()))));
    }
    return OrderQueryResponse.from(savedOrder, savedOrderLineItems);
  }

  public List<OrderQueryResponse> list() {
    final List<Order> orders = orderDao.findAll();
    final List<OrderQueryResponse> result = new ArrayList<>();
    for (final Order order : orders) {
      final List<OrderLineItemQueryResponse> orderLineItemQueryResponses =
          orderLineItemDao.findAllByOrderId(order.getId()).stream()
              .map(OrderLineItemQueryResponse::from).collect(
                  Collectors.toList());
      result.add(
          new OrderQueryResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
              order.getOrderedTime(), orderLineItemQueryResponses));
    }

    return result;
  }

  @Transactional
  public OrderQueryResponse changeOrderStatus(final Long orderId,
      final OrderStatusModifyRequest request) {
    final Order savedOrder = orderDao.findById(orderId)
        .orElseThrow(IllegalArgumentException::new);

    if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
      throw new IllegalArgumentException();
    }

    final OrderStatus orderStatus = request.getOrderStatus();
    final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);

    final Order result = new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
        orderStatus.name(), savedOrder.getOrderedTime(), orderLineItems);

    orderDao.save(result);

    final List<OrderLineItemQueryResponse> orderLineItemResponses = orderLineItems.stream()
        .map(OrderLineItemQueryResponse::from)
        .collect(Collectors.toList());
    return OrderQueryResponse.from(result, orderLineItemResponses);
  }
}

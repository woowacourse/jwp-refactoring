package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.persistence.MenuDao;
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
  public Order create(final Order order) {
    final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

    if (CollectionUtils.isEmpty(orderLineItems)) {
      throw new IllegalArgumentException();
    }

    final List<Long> menuIds = orderLineItems.stream()
        .map(OrderLineItem::getMenuId)
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
    final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
    for (final OrderLineItem orderLineItem : orderLineItems) {
      savedOrderLineItems.add(orderLineItemDao.save(
          new OrderLineItem(orderLineItem.getSeq(), orderId, orderLineItem.getMenuId(),
              orderLineItem.getQuantity())));
    }
    return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
        savedOrder.getOrderedTime(), savedOrderLineItems);
  }

  public List<Order> list() {
    final List<Order> orders = orderDao.findAll();
    final List<Order> result = new ArrayList<>();
    for (final Order order : orders) {
      result.add(new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
          order.getOrderedTime(), orderLineItemDao.findAllByOrderId(order.getId())));
    }

    return result;
  }

  @Transactional
  public Order changeOrderStatus(final Long orderId, final Order order) {
    final Order savedOrder = orderDao.findById(orderId)
        .orElseThrow(IllegalArgumentException::new);

    if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
      throw new IllegalArgumentException();
    }

    final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
    final Order result = new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
        orderStatus.name(), savedOrder.getOrderedTime());

    orderDao.save(result);
    return result;
  }
}

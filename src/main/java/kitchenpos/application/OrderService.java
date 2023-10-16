package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
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

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        order.setId(null);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus changedOrderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(changedOrderStatus.name());
        savedOrder.setOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}

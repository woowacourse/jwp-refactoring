package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final Order order) {
        order.validateNotEmptyOrderLineItems();
        validateOrderLineItemMatchMenu(order);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(NotFoundOrderTableException::new);
        orderTable.validateOrderable();

        final Order newOrder = new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderRepository.save(newOrder);

        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(order.getOrderLineItems(), savedOrder);

        savedOrder.updateOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    private void validateOrderLineItemMatchMenu(final Order order) {
        final List<Long> menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        order.validateOrderLineItemSize(menuRepository.countByIdIn(menuIds));
    }

    private List<OrderLineItem> saveOrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();
        for (final Order order : orders) {
            order.updateOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        savedOrder.updateOrderStatus(OrderStatus.valueOf(order.getOrderStatus()).name());
        savedOrder.updateOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));
        return savedOrder;
    }
}

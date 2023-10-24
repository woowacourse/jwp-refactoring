package kitchenpos.application.order;

import kitchenpos.application.order.request.OrderCreateRequest;
import kitchenpos.application.order.request.OrderLineItemCreateRequest;
import kitchenpos.application.order.request.OrderUpdateRequest;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderDao;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderDao orderDao,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItemCreateRequest> orderLineItems = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        long menuCounts = menuRepository.countByIdIn(menuIds);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateEmptiness();

        Order order = mapToOrder(request, menuCounts);
        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItems) {
            OrderLineItem orderLineItem = mapToOrderLineItem(orderLineItemCreateRequest, orderId);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private Order mapToOrder(OrderCreateRequest request, long menuCounts) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(this::mapToOrderLineItem)
                .collect(Collectors.toList());

        return Order.of(
                request.getOrderTableId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems,
                menuCounts
        );
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemCreateRequest orderLineItem) {
        return OrderLineItem.of(
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemCreateRequest orderLineItem, Long orderId) {
        return OrderLineItem.of(
                orderId,
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderUpdateRequest request) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(request.getOrderStatus());

        orderDao.save(order);

        order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return order;
    }
}

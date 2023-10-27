package kitchenpos.order;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.order.request.OrderLineItemCreateRequest;
import kitchenpos.order.request.OrderUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, OrderValidator orderValidator, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = request.getOrderLineItems();

        orderValidator.validate(request.getOrderTableId(), mapToOrderLineItems(orderLineItemCreateRequests));
        Order order = OrderMapper.mapToOrder(request);
        Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItemCreateRequests) {
            OrderLineItem orderLineItem = mapToOrderLineItem(orderLineItemCreateRequest, savedOrder.getId());
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return orderRepository.save(savedOrder);
    }

    private static List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemCreateRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemCreateRequest orderLineItem, Long orderId) {
        Long menuId = orderLineItem.getMenuId();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);

        return OrderLineItem.of(
                orderId,
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity(),
                menu.getName(),
                menu.getPrice()
        );
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderUpdateRequest request) {
        final Order order = getOrder(orderId);
        order.changeOrderStatus(request.getOrderStatus());

        orderRepository.save(order);
        order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return order;
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

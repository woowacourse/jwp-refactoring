package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.jpa.MenuRepository;
import kitchenpos.dao.jpa.OrderLineItemRepository;
import kitchenpos.dao.jpa.OrderRepository;
import kitchenpos.dao.jpa.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        validateOrderLineItemIsNotEmpty(orderLineItemRequests);
        validateOrderLineItemAllExists(orderLineItemRequests);
        final Order savedOrder = createOrder(request);

        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            OrderLineItem orderLineItem = createOrderLineItem(orderLineItemRequest, savedOrder);
            orderLineItemRepository.save(orderLineItem);
        }
        return OrderResponse.from(savedOrder);
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest, Order savedOrder) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(savedOrder);

        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow();

        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(orderLineItemRequest.getQuantity());
        return orderLineItem;
    }

    private Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private void validateOrderLineItemAllExists(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            System.out.println(orderLineItemRequests.size());
            System.out.println(menuRepository.countByIdIn(menuIds));
            System.out.println(menuIds);
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemIsNotEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}

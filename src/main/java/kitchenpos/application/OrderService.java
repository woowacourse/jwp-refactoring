package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
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
            createOrderLineItem(orderLineItemRequest, savedOrder);
        }
        return OrderResponse.from(savedOrder);
    }

    private void createOrderLineItem(OrderLineItemRequest orderLineItemRequest, Order savedOrder) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow();

        OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu, orderLineItemRequest.getQuantity());
        orderLineItemRepository.save(orderLineItem);
    }

    private Order createOrder(CreateOrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

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

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (OrderStatus.COMPLETION == order.getOrderStatus()) {
            throw new IllegalArgumentException();
        }

        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.from(order);
    }
}

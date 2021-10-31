package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusModifyRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final List<OrderLineItem> orderLineItems = generateOrderLineItems(orderRequest.getOrderLineItems());

        final Order order = new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(orderLineItems)
                .build();
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> generateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = findMenu(orderLineItemRequest.getMenuId());
            final OrderLineItem orderLineItem = new OrderLineItem.Builder()
                    .menu(menu)
                    .quantity(orderLineItemRequest.getQuantity())
                    .build();
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllFetchJoinOrderLineItems();
        return OrderResponse.toList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusModifyRequest orderStatusModifyRequest) {
        final Order foundOrder = findOrder(orderId);
        foundOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusModifyRequest.getOrderStatus()));
        return OrderResponse.of(foundOrder);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

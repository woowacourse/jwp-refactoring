package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = getOrderLineItems(request);

        Order order = orderRepository.save(Order.of(orderTable.getId(), orderLineItems, orderTable.isEmpty()));
        orderTable.addOrder(order.getId(), order.getOrderStatus());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest request) {
        List<OrderLineItemRequest> itemRequests = request.getOrderLineItemRequests();
        validateOrderLineItem(itemRequests);

        return itemRequests.stream()
                .map(itemRequest -> new OrderLineItem(itemRequest.getMenuId(), itemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrderLineItem(List<OrderLineItemRequest> itemRequests) {
        List<Long> menuIds = toMenuIds(itemRequests);
        if (!menuRepository.existsAllByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다 : " + menuIds);
        }
    }

    private List<Long> toMenuIds(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest request) {
        Order savedOrder = orderRepository.getById(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }
}

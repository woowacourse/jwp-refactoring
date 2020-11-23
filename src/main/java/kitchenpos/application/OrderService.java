package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                        OrderLineItemService orderLineItemService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        validateOrderLineItemNotEmpty(orderLineItemRequests);
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 order table id 입니다."));
        Order savedOrder = orderRepository.save(Order.cooking(orderTable));
        List<OrderLineItem> orderLineItems = orderLineItemService.createOrderLineItems(savedOrder, orderLineItemRequests);
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private void validateOrderLineItemNotEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 내역이 비어있습니다.");
        }
    }

    public List<OrderResponse> list() {
        List<OrderLineItem> orderLineItems = orderLineItemService.findAll();
        Set<Order> orders = extractDistinctOrder(orderLineItems);
        return orders.stream()
                .map(order -> OrderResponse.of(order, selectiveByOrder(order, orderLineItems)))
                .collect(Collectors.toList());
    }

    private Set<Order> extractDistinctOrder(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getOrder)
                .collect(Collectors.toSet());
    }

    private List<OrderLineItem> selectiveByOrder(Order order, List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.equalsByOrderId(order.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, Order order) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 order id 입니다."));
        Order changedOrder = savedOrder.changeOrderStatus(order.getOrderStatus());
        return orderRepository.save(changedOrder);
    }
}

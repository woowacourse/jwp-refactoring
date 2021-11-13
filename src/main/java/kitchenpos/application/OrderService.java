package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = saveOrderLineItems(request.getOrderLineItemRequests());
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        Order order = orderRepository.save(Order.cooking(orderTable, orderLineItems));
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(it -> {
                    if (!menuRepository.existsById(it.getMenuId())) throw new IllegalArgumentException();
                    return orderLineItemRepository.save(it.toEntity());
                }).collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}

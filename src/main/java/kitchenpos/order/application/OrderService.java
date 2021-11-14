package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository, OrderTableRepository orderTableRepository, OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    public OrderResponse create(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        orderValidator.validate(orderTable);

        Order order = Order.cooking(orderTable, saveOrderLineItems(request.getOrderLineItemRequests()));
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream().map(this::saveOrderLineItem).collect(Collectors.toList());
    }

    private OrderLineItem saveOrderLineItem(OrderLineItemRequest request) {
        if (!menuRepository.existsById(request.getMenuId())) {
            throw new IllegalArgumentException();
        }
        return orderLineItemRepository.save(request.toEntity());
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}

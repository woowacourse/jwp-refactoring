package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        Long orderTableId = request.getOrderTableId();
        OrderTable orderTable = orderTableRepository.getById(orderTableId);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItemCreateRequest : request.getOrderLineItems()) {
            Menu menu = menuRepository.getById(orderLineItemCreateRequest.getMenuId());
            long quantity = orderLineItemCreateRequest.getQuantity();
            OrderLineItem orderLineItem = new OrderLineItem(menu, quantity);
            orderLineItems.add(orderLineItem);
        }

        Order order = new Order(orderTable, orderLineItems);
        Order savedOrder = orderRepository.save(order);
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> readAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderUpdateRequest request) {
        Order savedOrder = orderRepository.getById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}

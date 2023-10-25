package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
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
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = createOrderLineItems(request);

        Order order = orderRepository.save(new Order(orderTable, orderLineItems));
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest request : orderCreateRequest.getOrderLineItems()) {
            Menu menu = menuRepository.getById(request.getMenuId());
            long quantity = request.getQuantity();
            OrderLineItem orderLineItem = new OrderLineItem(menu, quantity);
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public List<OrderResponse> readAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}

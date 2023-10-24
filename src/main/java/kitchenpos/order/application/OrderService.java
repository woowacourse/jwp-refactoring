package kitchenpos.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.request.OrderCreationRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.dto.response.OrderLineItemResponse;
import kitchenpos.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderCreationRequest request) {
        OrderLineItems orderLineItems = createOrderLineItems(request.getOrderLineItemRequests());
        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        Order order = Order.create(orderTable, orderLineItems, orderValidator);

        orderRepository.save(order);

        return mapToOrderResponse(order);
    }

    private OrderLineItems createOrderLineItems(List<OrderLineItemRequest> requests) {
        List<OrderLineItem> orderLineItems = requests.stream()
                .map(request -> OrderLineItem.create(request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());

        return OrderLineItems.from(orderLineItems);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문 테이블이 존재하지 않습니다."));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.from(order, orderLineItemResponses);
    }

    private OrderLineItemResponse mapToOrderLineItemResponse(OrderLineItem orderLineItem) {
        Menu menu = findMenuById(orderLineItem.getMenuId());

        return OrderLineItemResponse.from(orderLineItem, menu);
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new NoSuchElementException("menuId에 해당하는 값이 존재하지 않습니다."));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = findOrderById(orderId);
        String orderStatus = request.getOrderStatus();

        order.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return mapToOrderResponse(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문이 존재하지 않습니다."));
    }

}

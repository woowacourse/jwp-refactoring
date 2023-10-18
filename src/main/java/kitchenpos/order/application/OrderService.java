package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemInfo;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderTableRepository orderTableRepository, OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = orderLineItems(request.getOrderLineItems());
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        return OrderResponse.from(orderRepository.save(
                new Order(orderTable, orderLineItems, orderValidator)
        ));
    }

    private List<OrderLineItem> orderLineItems(List<OrderLineItemInfo> orderLineItems) {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .toList();
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest request) {
        Order savedOrder = orderRepository.getById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}

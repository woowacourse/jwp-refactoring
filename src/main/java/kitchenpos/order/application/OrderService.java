package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.order.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findByIdOrThrow(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        Order order = orderRepository.save(Order.create(orderTable, orderLineItems, orderValidator));
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(orderLineItem -> new OrderLineItem(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAllWithFetch().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderChangeStatusRequest request) {
        Order order = orderRepository.findByIdOrThrow(orderId);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}

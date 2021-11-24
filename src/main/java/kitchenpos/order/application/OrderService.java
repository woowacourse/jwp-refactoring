package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.ui.request.ChangeOrderStatusRequest;
import kitchenpos.order.ui.request.CreateOrderRequest;
import kitchenpos.order.ui.response.CreateOrderResponse;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public CreateOrderResponse create(final CreateOrderRequest request) {
        orderValidator.validateTable(request.getOrderTableId());
        final Order order = getOrder(request);
        return CreateOrderResponse.from(orderRepository.save(order));
    }

    private Order getOrder(CreateOrderRequest request) {
        final Order order = new Order(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                                             .stream()
                                             .map(item -> {
                                                 orderValidator.validateMenu(item.getMenuId());
                                                 return new OrderLineItem(order, item.getMenuId(), item.getQuantity());
                                             })
                                             .collect(Collectors.toList());
        order.addOrderLineItem(orderLineItems);
        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                              .stream()
                              .map(OrderResponse::from)
                              .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문의 상태는 변경할 수 없습니다."));

        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}

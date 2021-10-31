package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, OrderLineItemService orderLineItemService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order savedOrder = saveOrders(orderRequest);
        List<OrderLineItem> orderLineItems
                = orderLineItemService.create(orderRequest.getOrderLineItems(), savedOrder);
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private Order saveOrders(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NonExistentException("주문 테이블을 찾을 수 없습니다."));
        Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<OrderResponse> list() {
        Map<Order, List<OrderLineItem>> results = new HashMap<>();
        final List<Order> orders = orderRepository.findAll();
        for (final Order order : orders) {
            results.put(order, orderLineItemService.findAllByOrderId(order.getId()));
        }
        return OrderResponse.from(results);
    }

    @Transactional
    public OrderStatusResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order updateOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NonExistentException("주문을 찾을 수 없습니다."));
        validateOrderStatus(updateOrder);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        updateOrder.updateOrderStatus(orderStatus.name());
        return OrderStatusResponse.from(updateOrder.getOrderStatus());
    }

    private void validateOrderStatus(Order updateOrder) {
        if (!updateOrder.isNotCompleted()) {
            throw new IllegalArgumentException("주문이 이미 완료된 상태입니다.");
        }
    }
}

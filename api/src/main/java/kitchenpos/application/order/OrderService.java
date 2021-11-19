package kitchenpos.application.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.dto.order.OrderDetailResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import kitchenpos.dto.order.OrderStatusResponse;
import kitchenpos.exception.NonExistentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        OrderLineItemService orderLineItemService
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order savedOrder = createOrder(orderRequest);
        orderLineItemService.createOrderLineItem(orderRequest.getOrderLineItems(), savedOrder);
        return OrderResponse.from(savedOrder);
    }

    private Order createOrder(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NonExistentException("주문 테이블을 찾을 수 없습니다."));
        Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderStatusResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order updateOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NonExistentException("주문을 찾을 수 없습니다."));
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        updateOrder.updateOrderStatus(orderStatus.name());
        return OrderStatusResponse.from(updateOrder.getOrderStatus());
    }

    public OrderDetailResponse orderDetailInfo(Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NonExistentException("주문을 찾을 수 없습니다."));
        return OrderDetailResponse.from(order);
    }
}

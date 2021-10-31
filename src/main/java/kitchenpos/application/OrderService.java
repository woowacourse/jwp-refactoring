package kitchenpos.application;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NotFoundException;
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
        Orders savedOrders = saveOrders(orderRequest);
        List<OrderLineItem> orderLineItems
                = orderLineItemService.create(orderRequest.getOrderLineItems(), savedOrders);
        return OrderResponse.of(savedOrders, orderLineItems);
    }

    private Orders saveOrders(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NotFoundException("주문 테이블을 찾을 수 없습니다."));
        Orders orders = new Orders(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        return orderRepository.save(orders);
    }

    public List<OrderResponse> list() {
        Map<Orders, List<OrderLineItem>> results = new HashMap<>();
        final List<Orders> orders = orderRepository.findAll();
        for (final Orders order : orders) {
            results.put(order, orderLineItemService.findAllByOrderId(order.getId()));
        }
        return OrderResponse.from(results);
    }

    @Transactional
    public OrderStatusResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Orders updateOrders = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));
        validateOrderStatus(updateOrders);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        updateOrders.updateOrderStatus(orderStatus.name());
        return OrderStatusResponse.from(updateOrders.getOrderStatus());
    }

    private void validateOrderStatus(Orders updateOrders) {
        if (!updateOrders.isNotCompleted()) {
            throw new IllegalArgumentException("주문이 이미 완료된 상태입니다.");
        }
    }
}

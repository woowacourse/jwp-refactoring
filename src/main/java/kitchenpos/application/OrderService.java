package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemService orderLineItemService;
    private final MenuService menuService;

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을 수 없습니다."));

        if (orderTable.getEmpty()) {
            throw new IllegalArgumentException("해당 테이블이 비어있습니다.");
        }
        final Order order = new Order(orderTable, OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);

        orderLineItemService.createOrderLineItems(order, orderLineItemRequests);

        return transferOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::transferOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }

        final OrderStatus orderStatus = OrderStatus.findOrderStatus(request.getOrderStatus());
        final Order updatedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus, savedOrder.getOrderedTime());

        orderRepository.save(updatedOrder);

        return transferOrderResponse(updatedOrder);
    }

    private OrderResponse transferOrderResponse(Order order) {
        List<MenuResponse> menuResponses = menuService.findMenusByOrder(order);

        return new OrderResponse(order, menuResponses);
    }
}

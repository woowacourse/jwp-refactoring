package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.request.OrderCreateRequest;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.dto.OrderLineItemDto;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }

        Order order = Order.builder()
                .orderTableId(orderCreateRequest.getOrderTableId())
                .orderLineItems(orderCreateRequest.convertToOrderLineItems())
                .orderStatus(OrderStatus.COOKING)
                .build();
        orderValidator.validate(order);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문을 찾을 수 없습니다."));

        if (foundOrder.isCompleted()) {
            throw new IllegalArgumentException("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        String orderStatusRequest = orderStatusUpdateRequest.getOrderStatus();
        OrderStatus orderStatus = OrderStatus.from(orderStatusRequest);
        foundOrder.updateStatus(orderStatus);
        return OrderResponse.from(foundOrder);
    }
}

package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dao.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderTableRepository orderTableRepository,
                        OrderValidator orderValidator,
                        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCommand orderCommand) {
        OrderTable orderTable = getOrderTable(orderCommand.getOrderTableId());
        List<OrderLineItem> orderLineItems = orderCommand.toEntity();
        orderValidator.validate(orderLineItems);
        return OrderResponse.from(orderRepository.save(Order.first(orderTable, orderLineItems)));
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String status) {
        Order savedOrder = getOrder(orderId);
        savedOrder.changeStatus(OrderStatus.valueOf(status));
        return OrderResponse.from(savedOrder);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}

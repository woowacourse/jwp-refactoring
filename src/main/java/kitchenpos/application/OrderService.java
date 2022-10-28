package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderLineItemDao orderLineItemDao,
                        OrderTableRepository orderTableRepository,
                        OrderValidator orderValidator,
                        OrderRepository orderRepository) {
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCommand orderCommand) {
        List<OrderLineItemCommand> orderLineItems = orderCommand.getOrderLineItems();
        Long orderTableId = orderCommand.getOrderTableId();
        orderValidator.validateCreate(orderCommand);

        OrderTable orderTable = getOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }

        Order order = new Order(orderTable.getId(), OrderStatus.COOKING);

        Order savedOrder = orderRepository.save(order);

        final Long orderId = savedOrder.getId();
        List<OrderLineItem> collect = orderLineItems.stream()
                .map(it -> new OrderLineItem(orderId, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        order.setOrderLineItems(collect);
        return OrderResponse.from(savedOrder);
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
        return orderRepository.findByIdOrderByOrderLineItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}

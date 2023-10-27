package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.application.dto.OrderTableGroupEventDto;
import kitchenpos.order.application.dto.OrderTableUnGroupEventDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(final OrderRepository orderRepository, final OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        return OrderResponse.of(orderMapper.toOrderTables(request));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.getById(orderId);

        savedOrder.setOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.of(savedOrder);
    }

    @EventListener
    public void group(OrderTableGroupEventDto event) {
        List<OrderTable> orderTables = orderMapper.toOrderTables(event.getOrderTableIds());
        for (OrderTable orderTable : orderTables) {
            orderTable.attachTableGroup(event.getTableGroupId());
        }
    }

    @EventListener
    public void ungroup(OrderTableUnGroupEventDto event) {
        List<OrderTable> orderTables = orderMapper.toOrderTables(event.getOrderTableIds());
        for (OrderTable orderTable : orderTables) {
            orderTable.detachTableGroup();
        }
    }
}

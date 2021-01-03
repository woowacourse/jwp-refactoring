package kitchenpos.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.enums.OrderStatus;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블을 찾을 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 안됩니다.");
        }

        final Order order = orderCreateRequest.toEntity(orderTable);

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
            final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을수 없습니다."));

        if (savedOrder.isCompletionStatus()) {
            throw new IllegalArgumentException("주문완료된 상태에서 상태를 변경할수 없습니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderChangeStatusRequest.getOrderStatus());

        savedOrder.setOrderStatus(orderStatus);
        
        orderRepository.save(savedOrder);

        return OrderResponse.of(savedOrder);
    }
}

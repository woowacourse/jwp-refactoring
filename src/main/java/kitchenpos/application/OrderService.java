package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTableById(request);
        final Order order = request.toOrder(LocalDateTime.now());
        orderTable.addOrder(order);
        orderTableRepository.flush();
        return OrderResponse.from(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.from(request.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private OrderTable findOrderTableById(final OrderCreateRequest request) {
        return orderTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException(CustomError.TABLE_NOT_FOUND_ERROR));
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(CustomError.ORDER_NOT_FOUND_ERROR));
    }
}

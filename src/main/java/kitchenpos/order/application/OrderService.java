package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.OrderChangeStatusRequest;
import kitchenpos.order.ui.dto.OrderCreateRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;

@Service
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
        orderTableRepository.flush(); // TODO: 2022/10/28 고민중..
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
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.ORDER_NOT_FOUND_ERROR));
    }
}

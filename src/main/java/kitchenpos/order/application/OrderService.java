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
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.ui.dto.OrderChangeStatusRequest;
import kitchenpos.order.ui.dto.OrderCreateRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    public OrderService(final TableRepository tableRepository, final OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
        final Order order = request.toOrder(LocalDateTime.now());
        orderTable.addOrder(order);
        tableRepository.flush();
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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.ORDER_NOT_FOUND_ERROR));
        order.changeStatus(OrderStatus.from(request.getOrderStatus()));
        return OrderResponse.from(order);
    }
}

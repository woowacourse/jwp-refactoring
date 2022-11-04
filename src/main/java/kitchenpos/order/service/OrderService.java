package kitchenpos.order.service;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.dto.response.OrdersResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderMapper orderMapper,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        Order order = orderMapper.mappingToOrder(request);
        validateExistOrderTable(order);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private void validateExistOrderTable(final Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public OrdersResponse list() {
        List<Order> orders = orderRepository.findAll();
        return OrdersResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}

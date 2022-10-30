package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderSaveRequest;
import kitchenpos.dto.OrderChangeOrderStatusRequest;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderSaveRequest request) {
        Order savedOrder = orderRepository.save(new Order(request.getOrderTableId(), toOrderLineItems(request)));
        return new OrderResponse(savedOrder);
    }

    private List<OrderLineItem> toOrderLineItems(final OrderSaveRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.getById(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        return new OrderResponse(savedOrder);
    }
}

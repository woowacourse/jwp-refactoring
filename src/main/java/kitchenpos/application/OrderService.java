package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemSaveRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderSaveRequest;
import kitchenpos.dto.OrderChangeOrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
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
                .map(this::toOrderLineItem)
                .collect(toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemSaveRequest it) {
        Menu menu = menuRepository.getById(it.getMenuId());
        return new OrderLineItem(menu.getName(), menu.getPrice(), it.getQuantity());
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
        savedOrder.changeOrderStatus(request.getOrderStatus());

        return new OrderResponse(savedOrder);
    }
}

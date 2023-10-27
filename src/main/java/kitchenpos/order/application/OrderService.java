package kitchenpos.order.application;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.UpdateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest createOrderRequest) {
        validateMenuIds(createOrderRequest.getMenuIds());

        final Order order = orderRepository.save(Order.createBy(createOrderRequest.getOrderTableId()));
        addOrderLineItem(createOrderRequest, order);
        return order;
    }

    private void addOrderLineItem(final CreateOrderRequest createOrderRequest, final Order order) {
        for (final OrderLineItemRequest orderLineItemRequest : createOrderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow();
            final OrderLineItem orderLineItem = new OrderLineItem(order, orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity(), menu.getName(), menu.getPrice());
            order.addOrderLineItem(orderLineItem);
        }
    }

    private void validateMenuIds(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final UpdateOrderRequest updateOrderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.updateStatus(updateOrderRequest.getOrderStatus());
        return orderRepository.save(order);
    }
}

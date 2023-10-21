package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderLineItemEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        OrderTable orderTable = orderTableRepository.findById(req.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);

        req.getOrderLineItems()
                .stream()
                .map(it -> menuRepository.findById(it.getMenuId())
                        .orElseThrow(MenuNotFoundException::new))
                .collect(Collectors.toList());

        List<OrderLineItem> orderLineItems = req.getOrderLineItems()
                .stream()
                .map(it -> orderLineItemRepository.save(new OrderLineItem(it.getMenuId(), it.getQuantity())))
                .collect(Collectors.toList());

        if (orderLineItems.size() != req.getOrderLineItems().size()) {
            throw new OrderLineItemEmptyException();
        }

        Order order = Order.createDefault(orderTable.getId(), orderLineItems);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest req) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(req.getOrderStatus()).name());

        return savedOrder;
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}

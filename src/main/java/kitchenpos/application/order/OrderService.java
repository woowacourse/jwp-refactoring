package kitchenpos.application.order;

import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.application.order.dto.OrderLineItemCreateRequest;
import kitchenpos.application.order.dto.OrderUpdateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        Order order = makeOrder(req.getOrderTableId());

        List<OrderLineItem> orderLineItems = getOrderLineItems(req.getOrderLineItems(), order);
        order.initOrderItems(orderLineItems);

        return order;
    }

    private Order makeOrder(final Long id) {
        OrderTable orderTable = findOrderTable(id);

        return orderRepository.save(new Order(null, orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), null));
    }

    private OrderTable findOrderTable(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> getOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests, final Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemCreateRequest orderLineReq : orderLineItemCreateRequests) {
            Menu menu = findMenu(orderLineReq.getMenuId());
            orderLineItems.add(new OrderLineItem(order, menu, orderLineReq.getQuantity()));
        }

        return orderLineItems;
    }

    private Menu findMenu(final Long id) {
        return menuRepository.findById(id)
                .orElseThrow(MenuNotFoundException::new);
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

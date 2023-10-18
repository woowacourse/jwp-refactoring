package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.jpa.JpaMenuRepository;
import kitchenpos.dao.jpa.JpaOrderLineItemRepository;
import kitchenpos.dao.jpa.JpaOrderRepository;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.CreateOrderLineItemRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final JpaMenuRepository jpaMenuRepository;
    private final JpaOrderRepository orderRepository;
    private final JpaOrderLineItemRepository orderLineItemRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public OrderService(
            JpaMenuRepository jpaMenuRepository,
            JpaOrderRepository orderRepository,
            JpaOrderLineItemRepository orderLineItemRepository,
            JpaOrderTableRepository orderTableRepository
    ) {
        this.jpaMenuRepository = jpaMenuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.getById(request.orderTableId());

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (CreateOrderLineItemRequest orderLineItemRequest : request.createOrderLineItemRequests()) {
            Menu menu = jpaMenuRepository.getById(orderLineItemRequest.menuId());
            OrderLineItem orderLineItem = new OrderLineItem(null, menu, orderLineItemRequest.quantity());
            orderLineItems.add(orderLineItem);
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order savedOrder = orderRepository.getById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return savedOrder;
    }
}

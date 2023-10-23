package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaOrderLineItemRepository;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderLineRequest;
import kitchenpos.ui.dto.request.OrderRequest;
import kitchenpos.ui.dto.request.OrderStatusRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final JpaMenuRepository jpaMenuRepository;
    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderLineItemRepository jpaOrderLineItemRepository;
    private final JpaOrderTableRepository jpaOrderTableRepository;

    public OrderService(
            final JpaMenuRepository jpaMenuRepository,
            final JpaOrderRepository jpaOrderRepository,
            final JpaOrderLineItemRepository jpaOrderLineItemRepository,
            final JpaOrderTableRepository jpaOrderTableRepository
    ) {
        this.jpaMenuRepository = jpaMenuRepository;
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaOrderLineItemRepository = jpaOrderLineItemRepository;
        this.jpaOrderTableRepository = jpaOrderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineRequest> orderLineRequests = request.getOrderLineRequests();

        if (request.getOrderTableId() == null) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = jpaOrderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineRequest orderLineRequest : orderLineRequests) {
            if (orderLineRequest.getMenuId() == null) {
                throw new IllegalArgumentException();
            }
            Menu menu = jpaMenuRepository.findById(orderLineRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            orderLineItems.add(new OrderLineItem(order, menu, orderLineRequest.getQuantity()));
        }

        order.setOrderLineItems(orderLineItems);

        final Order savedOrder = jpaOrderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(jpaOrderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = jpaOrderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(jpaOrderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = jpaOrderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());
        jpaOrderRepository.save(savedOrder);
        savedOrder.setOrderLineItems(jpaOrderLineItemRepository.findAllByOrderId(orderId));

        return new OrderResponse(savedOrder);
    }
}

package kitchenpos.order;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.JpaMenuRepository;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.JpaOrderTableRepository;
import kitchenpos.ordertable.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

        Order order = new Order(orderTable, OrderStatus.COOKING);

        final Order savedOrder = jpaOrderRepository.save(order);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineRequest orderLineRequest : orderLineRequests) {
            orderLineItems.add(
                    new OrderLineItem(savedOrder, orderLineRequest.getMenuId(), orderLineRequest.getQuantity()));
        }

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            if (orderLineItem.getMenuId() == null) {
                throw new IllegalArgumentException();
            }

            jpaMenuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);

            savedOrderLineItems.add(jpaOrderLineItemRepository.save(orderLineItem));
        }

        return new OrderResponse(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = jpaOrderRepository.findAll();

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderLineItem> orderLineItems = jpaOrderLineItemRepository.findAllByOrderId(order.getId());
            orderResponses.add(new OrderResponse(order, orderLineItems));
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = jpaOrderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());
        jpaOrderRepository.save(savedOrder);
        List<OrderLineItem> orderLineItems = jpaOrderLineItemRepository.findAllByOrderId(orderId);
        return new OrderResponse(savedOrder, orderLineItems);
    }
}

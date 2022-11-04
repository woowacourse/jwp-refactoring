package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;

    public OrderService(final OrderDao orderDao,
                        final OrderLineItemDao orderLineItemDao,
                        final OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final Long orderTableId = orderCreateRequest.getOrderTableId();
        final List<OrderLineItemRequest> orderLineItemRequests = orderCreateRequest.getOrderLineItems();
        orderValidator.validator(orderTableId, orderLineItemRequests);
        final Order savedOrder = orderDao.save(orderCreateRequest.toOrder());
        final List<OrderLineItem> orderLineItems = saveOrderLineItems(orderLineItemRequests, savedOrder);
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> saveOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                                   final Order savedOrder) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(savedOrder.getId(),
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .map(this::saveAndGetOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem saveAndGetOrderLineItem(final OrderLineItem orderLineItem) {
        return orderLineItemDao.save(orderLineItem);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        final List<OrderResponse> orderResponses = new ArrayList<>();
        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            orderResponses.add(OrderResponse.of(order, orderLineItems));
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatusName) {
        final Order savedOrder = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatusName);
        orderDao.save(savedOrder);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, orderLineItems);
    }
}

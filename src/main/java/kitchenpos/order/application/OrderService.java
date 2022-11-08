package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.request.OrderLineItemRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.application.validator.OrderValidator;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderMenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderMenuDao orderMenuDao;
    private final OrderValidator orderValidator;

    public OrderService(final OrderDao orderDao, final OrderMenuDao orderMenuDao,
        final OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderMenuDao = orderMenuDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request.getOrderLineItemRequests());
        Order order = new Order(request.getOrderTableId(), orderLineItems);
        orderValidator.validate(order);
        return OrderResponse.from(orderDao.save(order));
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
            .map(this::mapToOrderLineItem)
            .collect(Collectors.toUnmodifiableList());
    }

    private OrderLineItem mapToOrderLineItem(final OrderLineItemRequest orderLineItemRequest) {
        OrderMenu orderMenu = orderMenuDao.getByMenuId(orderLineItemRequest.getMenuId());
        return new OrderLineItem(orderMenu.getId(), orderLineItemRequest.getQuantity());
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}

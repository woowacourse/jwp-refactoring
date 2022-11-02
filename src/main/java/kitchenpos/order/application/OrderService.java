package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dao.MenuOrderDao;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderValidator;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderLineItemDao orderLineItemDao;
    private final MenuOrderDao menuOrderDao;

    public OrderService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final OrderLineItemDao orderLineItemDao, final MenuOrderDao orderMenuDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderLineItemDao = orderLineItemDao;
        this.menuOrderDao = orderMenuDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.getById(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request);
        OrderValidator orderValidator = new OrderValidator();
        Order savedOrder = orderDao.save(new Order(orderTable));

        orderValidator.validateOrderLineItems(orderLineItems);
        saveOrderLineItems(orderLineItems, savedOrder);
        return OrderResponse.from(savedOrder);
    }

    private void saveOrderLineItems(final List<OrderLineItem> orderLineItems, final Order savedOrder) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemDao.save(
                new OrderLineItem(savedOrder.getId(), orderLineItem.getMenuOrderId(), orderLineItem.getQuantity())
            );
        }
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderRequest request) {
        return request.getOrderLineItemRequests().stream()
            .map(orderLineItemRequest -> new OrderLineItem(
                menuOrderDao.getById(orderLineItemRequest.getMenuId()).getId(),
                orderLineItemRequest.getQuantity()))
            .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromAll(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}

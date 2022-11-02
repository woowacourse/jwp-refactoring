package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dao.MenuOrderDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.request.OrderRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final MenuOrderDao menuOrderDao;

    public OrderService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final MenuOrderDao orderMenuDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.menuOrderDao = orderMenuDao;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.getById(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request);

        return orderDao.save(new Order(orderTable, orderLineItems));
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

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return savedOrder;
    }
}

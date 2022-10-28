package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.application.dto.request.OrderStatusUpdateRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(MenuDao menuDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao,
                        OrderRepository orderRepository, OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    public OrderResponse create(OrderRequest request) {
        Order order = request.toOrder();
        validateOrderItemSize(order.getOrderLineItems());
        validateOrderTable(order);

        return new OrderResponse(orderRepository.save(order));
    }

    private void validateOrderTable(Order order) {
        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderItemSize(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order savedOrder = validateExistOrder(orderId);

        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeStatus(orderStatus);

        orderRepository.update(savedOrder);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return new OrderResponse(savedOrder);
    }

    private Order validateExistOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

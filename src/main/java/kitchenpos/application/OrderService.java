package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(MenuDao menuDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao,
                        OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = orderDao.save(
                new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    OrderLineItem orderLineItem = new OrderLineItem(order.getId(), orderLineItemRequest.getMenuId(),
                            orderLineItemRequest.getQuantity());
                    return orderLineItemDao.save(orderLineItem);
                })
                .collect(Collectors.toList());

        return mapToOrderResponse(order, orderLineItems);
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                mapToOrderLineItemResponses(orderLineItems));
    }

    private List<OrderLineItemResponse> mapToOrderLineItemResponses(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(),
                        orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> {
                    List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
                    return mapToOrderResponse(order, orderLineItems);
                }).collect(Collectors.toList());
    }

    @Transactional
    public void changeOrderStatus(Long orderId, OrderStatusRequest orderRequest) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        orderDao.save(new Order(order.getId(), order.getOrderTableId(), orderStatus.name(), order.getOrderedTime()));
    }
}

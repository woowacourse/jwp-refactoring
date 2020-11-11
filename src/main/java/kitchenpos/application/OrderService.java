package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final TableDao tableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final TableDao tableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        validOrderCreateRequest(request);
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        final Order order = new Order(
            request.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            OrderLineItem orderLineItem = new OrderLineItem(
                savedOrder.getId(),
                orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity()
            );
            orderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private void validOrderCreateRequest(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = Optional.ofNullable(request.getOrderLineItems())
            .filter(orderItems -> !orderItems.isEmpty())
            .orElseThrow(IllegalArgumentException::new);

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
        tableDao.findById(request.getOrderTableId())
            .filter(table -> !table.isEmpty())
            .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
            .map(order -> OrderResponse.of(order, orderLineItemDao.findAllByOrderId(order.getId())))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .filter(Order::isNotCompleted)
            .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.of(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);
        return OrderResponse.of(savedOrder, orderLineItemDao.findAllByOrderId(orderId));
    }
}

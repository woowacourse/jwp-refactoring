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
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
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
        return OrderResponse.of(savedOrder, OrderLineItemResponse.of(orderLineItems));
    }

    private void validOrderCreateRequest(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = Optional.ofNullable(request.getOrderLineItems())
            .filter(orderItems -> !orderItems.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("하나 이상의 메뉴 및 수량을 지정하여 주문해주세요."));

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴를 주문할 수 없습니다.");
        }
        tableDao.findById(request.getOrderTableId())
            .filter(table -> !table.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("어떤 테이블이 주문하고자 하는지 지정해주세요."));
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
            .map(order -> OrderResponse.of(order, OrderLineItemResponse.of(
                orderLineItemDao.findAllByOrderId(order.getId()))))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .filter(Order::isNotCompleted)
            .orElseThrow(() -> new IllegalArgumentException(
                "식사가 완료된 테이블의 주문 상태를 변경할 수 없습니다."));

        final OrderStatus orderStatus = OrderStatus.of(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);
        return OrderResponse.of(savedOrder, OrderLineItemResponse.of(
            orderLineItemDao.findAllByOrderId(orderId)));
    }
}

package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.common.exception.InvalidMenuException;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
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
    public Long create(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);

        OrderTable orderTable = getOrderTable(orderRequest.getOrderTableId());
        validateOrderTable(orderTable);

        return orderDao.save(new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now()));
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
        validateOrderLineItem(orderLineItems);
        validateMenu(orderLineItems);
    }

    private void validateOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new InvalidOrderException("주문 항목이 비어있습니다.");
        }
    }

    private void validateMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new InvalidMenuException("메뉴가 존재하지 않습니다.");
        }
    }

    private OrderTable getOrderTable(Long orderId) {
        return orderTableDao.findById(orderId)
                .orElseThrow(() -> new InvalidTableException("테이블이 존재하지 않습니다."));
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableException("빈 테이블입니다.");
        }
    }

    private List<OrderLineItem> getOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems
                .stream()
                .map(orderLineItemRequest -> {
                    OrderLineItem orderLineItem = new OrderLineItem(order.getId(), orderLineItemRequest.getMenuId(),
                            orderLineItemRequest.getQuantity());
                    return orderLineItemDao.save(orderLineItem);
                })
                .collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(),
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
        OrderStatus orderStatus = OrderStatus.find(orderRequest.getOrderStatus());
        Order order = getOrder(orderId);
        validateOrder(order);
        orderDao.updateStatus(order.getId(), orderStatus);
    }

    private void validateOrder(Order order) {
        if (order.isCompleted()) {
            throw new InvalidOrderException("주문이 완료 상태입니다.");
        }
    }

    private Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("주문이 존재하지 않습니다."));
    }
}

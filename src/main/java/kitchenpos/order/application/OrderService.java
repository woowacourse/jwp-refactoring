package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.InvalidMenuException;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.OrderRequest;
import kitchenpos.order.application.dto.request.OrderStatusRequest;
import kitchenpos.order.application.dto.response.OrderLineItemResponse;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.InvalidTableException;
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

        Long orderId = orderDao.save(
                new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now()));
        saveOrderLineItems(orderId, orderRequest.getOrderLineItems());
        return orderId;
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

    private void saveOrderLineItems(Long orderId, List<OrderLineItemRequest> orderLineItems) {
        for (OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = getMenu(orderLineItem.getMenuId());
            orderLineItemDao.save(
                    new OrderLineItem(orderId, menu.getName(), menu.getPrice(), orderLineItem.getQuantity()));
        }
    }

    private Menu getMenu(Long menuId) {
        return menuDao.findById(menuId)
                .orElseThrow(() -> new InvalidMenuException("메뉴가 존재하지 않습니다."));
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(),
                mapToOrderLineItemResponses(orderLineItems));
    }

    private List<OrderLineItemResponse> mapToOrderLineItemResponses(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getName(), orderLineItem.getPrice(), orderLineItem.getQuantity()))
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
        order.changeStatus(orderStatus);
        orderDao.updateStatus(order.getId(), order.getOrderStatus());
    }

    private Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("주문이 존재하지 않습니다."));
    }
}

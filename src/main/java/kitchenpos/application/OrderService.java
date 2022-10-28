package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final MenuDao menuDao,
                        final OrderDao orderDao,
                        final OrderLineItemDao orderLineItemDao,
                        final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderCreateRequest.getOrderLineItems();

        validateEmptyOrderLineItemRequests(orderLineItemRequests);
        validateExistOrderLineItemRequestInMenu(orderLineItemRequests);

        final OrderTable orderTable = findOrderTable(orderCreateRequest);
        final Order savedOrder = orderDao.save(Order.from(orderTable));
        final List<OrderLineItem> orderLineItems = saveOrderLineItems(orderLineItemRequests, savedOrder);

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private OrderTable findOrderTable(final OrderCreateRequest orderCreateRequest) {
        return orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    private void validateEmptyOrderLineItemRequests(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }
    }

    private void validateExistOrderLineItemRequestInMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴는 주문할 수 없습니다.");
        }
    }

    private List<OrderLineItem> saveOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                                   final Order savedOrder) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> orderLineItemDao.save(new OrderLineItem(savedOrder.getId(),
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity())))
                .collect(Collectors.toList());
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
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatusName);
        orderDao.save(savedOrder);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, orderLineItems);
    }
}

package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItemRequest> orderLineItems = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }
//        orderLineItems.stream()
//                .map(orderLineItemRequest -> menuDao.findById(orderLineItemRequest.getMenuId()))
//                .filter(Optional::isPresent)
//                .collect(Collectors.toList());
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴는 주문할 수 없습니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity());
            order.addOrderLineItem(orderLineItemDao.save(orderLineItem));
        }

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        // TODO: 2depth 수정
        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            for (OrderLineItem item : orderLineItems) {
                order.addOrderLineItem(item);
            }
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final String orderStatusName) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatusName);
        orderDao.save(savedOrder);
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);
        for (OrderLineItem item : orderLineItems) {
            savedOrder.addOrderLineItem(item);
        }

        return savedOrder;
    }
}

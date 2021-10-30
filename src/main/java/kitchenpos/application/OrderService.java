package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusModifyRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Order create(final OrderRequest orderRequest) {
        // TODO: 오더라인 아이템 menuId 발리데이션
//        if (orderLineItemRequests.size() != menuDao.countByOrderLineItemsIn(orderLineItems)) {
//            throw new IllegalArgumentException();
//        }

        final Order order = new Order();
        order.setId(null);

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        // 오더를 받은 테이블이라면 사람이 있어야 말이 되는거겠지?
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrder(savedOrder);
            orderLineItem.setMenu(menuDao.findById(orderLineItemRequest.getMenuId()).get());
            orderLineItem.setQuantity(orderLineItemRequest.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }

        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrder(order));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusModifyRequest orderStatusModifyRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusModifyRequest.getOrderStatus()));

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrder(savedOrder));

        return savedOrder;
    }
}

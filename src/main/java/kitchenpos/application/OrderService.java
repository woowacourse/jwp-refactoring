package kitchenpos.application;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Order create(final Order orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        validateOrderTableEmpty(orderRequest.getOrderTableId());
        validateExistMenus(orderLineItems);

        final Order savedOrder = orderDao.save(Order.of(orderRequest.getOrderTableId(), orderLineItems));
        savedOrder.setOrderLineItems(getSavedOrderLineItems(orderLineItems, savedOrder));
        return savedOrder;
    }

    private void validateOrderTableEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비워져있으면 주문을 생성할 수 없습니다.");
        }
    }

    private void validateExistMenus(final List<OrderLineItem> orderLineItems) {
        validateExistOrderLineItems(orderLineItems);
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 상품 목록의 메뉴가 존재하지 않습니다.");
        }
    }

    private void validateExistOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            throw new IllegalArgumentException("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
        }
    }

    private List<OrderLineItem> getSavedOrderLineItems(final List<OrderLineItem> orderLineItems, final Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new LinkedList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemDao.save(new OrderLineItem(
                    savedOrder.getId(),
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            )));
        }
        return savedOrderLineItems;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.from(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}

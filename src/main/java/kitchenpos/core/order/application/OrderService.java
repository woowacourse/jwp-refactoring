package kitchenpos.core.order.application;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.core.menu.domain.MenuDao;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderChangeEvent;
import kitchenpos.core.order.domain.OrderDao;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderLineItemDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher publisher;


    public OrderService(final MenuDao menuDao,
                        final OrderDao orderDao,
                        final OrderLineItemDao orderLineItemDao,
                        final OrderValidator orderValidator,
                        final ApplicationEventPublisher publisher) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
        this.publisher = publisher;
    }

    @Transactional
    public Order create(final Order orderRequest) {
        final List<OrderLineItem> orderLineItemsRequest = orderRequest.getOrderLineItems();
        validateExistMenus(orderLineItemsRequest);
        final Order savedOrder = orderDao.save(Order.of(orderRequest.getOrderTableId(), orderLineItemsRequest, orderValidator));
        savedOrder.changeOrderLineItems(getSavedOrderLineItems(orderLineItemsRequest, savedOrder));
        return savedOrder;
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
            order.changeOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order orderRequest) {
        final Order order = getOrder(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        publisher.publishEvent(new OrderChangeEvent(orderId, order.getOrderTableId(), orderRequest.getOrderStatus().name()));
        return orderDao.save(order);
    }

    private Order getOrder(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }
}

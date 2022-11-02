package kitchenpos.core.order.application;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.core.menu.domain.MenuDao;
import kitchenpos.core.order.application.dto.OrderLineItemRequest;
import kitchenpos.core.order.application.dto.OrderRequest;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderChangeEvent;
import kitchenpos.core.order.domain.OrderDao;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderLineItemDao;
import kitchenpos.core.order.domain.OrderStatus;
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
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemsRequest = orderRequest.getOrderLineItemRequests();
        validateExistMenus(orderLineItemsRequest);

        final Order savedOrder = orderDao.save(Order.of(orderRequest.getOrderTableId(), mapToOrderLineItems(orderLineItemsRequest), orderValidator));
        savedOrder.changeOrderLineItems(getSavedOrderLineItems(orderLineItemsRequest, savedOrder));
        return savedOrder;
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> orderLineItemsRequest) {
        return orderLineItemsRequest.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateExistMenus(final List<OrderLineItemRequest> orderLineItemsRequest) {
        validateExistOrderLineItems(orderLineItemsRequest);
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsRequest.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 상품 목록의 메뉴가 존재하지 않습니다.");
        }
    }

    private void validateExistOrderLineItems(final List<OrderLineItemRequest> orderLineItemsRequest) {
        if (orderLineItemsRequest == null) {
            throw new IllegalArgumentException("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
        }
    }

    private List<OrderLineItem> getSavedOrderLineItems(final List<OrderLineItemRequest> orderLineItemsRequest, final Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new LinkedList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemsRequest) {
            savedOrderLineItems.add(orderLineItemDao.save(new OrderLineItem(
                    savedOrder.getId(),
                    orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity()
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
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = getOrder(orderId);
        order.changeOrderStatus(OrderStatus.from(orderRequest.getOrderStatus()));
        publisher.publishEvent(new OrderChangeEvent(orderId, order.getOrderTableId(), orderRequest.getOrderStatus()));
        return orderDao.save(order);
    }

    private Order getOrder(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }
}

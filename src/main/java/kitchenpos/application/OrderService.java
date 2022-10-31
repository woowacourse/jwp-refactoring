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
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderMenusCountException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
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
    public Order create(OrderCreateRequest orderCreateRequest) {
        validateOrderLineItems(orderCreateRequest.getOrderLineItems());
        validateOrderTable(orderCreateRequest.getOrderTableId());

        Order savedOrder = saveOrder(orderCreateRequest);
        List<OrderLineItem> savedOrderLineItems =
                getOrderLineItems(orderCreateRequest.getOrderLineItems(), savedOrder.getId());

        return new Order(savedOrder, savedOrderLineItems);
    }

    private void validateOrderLineItems(List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = getMenuIds(orderLineItemDtos);
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new OrderMenusCountException();
        }
        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new NotFoundMenuException();
        }
    }

    private List<Long> getMenuIds(List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private Order saveOrder(OrderCreateRequest orderCreateRequest) {
        return orderDao.save(new Order(
                orderCreateRequest.getOrderTableId(),
                OrderStatus.COOKING,
                LocalDateTime.now()
        ));
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemDto> orderLineItemDtos, Long orderId) {
        return orderLineItemDtos.stream()
                .map(orderLineItemDto -> orderLineItemDao.save(new OrderLineItem(
                        orderId,
                        orderLineItemDto.getMenuId(),
                        orderLineItemDto.getQuantity()
                )))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderDao.findAll().stream()
                .map(order -> new Order(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus()));

        return orderDao.save(order);
    }

    private Order findOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
    }
}

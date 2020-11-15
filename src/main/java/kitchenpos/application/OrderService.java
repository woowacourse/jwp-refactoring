package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;
import kitchenpos.ui.dto.OrderUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();
        requireOrderLineItemNotEmpty(orderLineItemRequests);
        requireMenusExist(orderLineItemRequests);
        requireOrderTableNotEmpty(request.getOrderTableId());

        final Order order = orderDao.save(request.toEntity(OrderStatus.COOKING.name(), LocalDateTime.now()));
        for (final OrderLineItemCreateRequest orderLineItemRequest : orderLineItemRequests) {
            order.add(orderLineItemDao.save(orderLineItemRequest.toEntity(order.getId())));
        }
        return order;
    }

    private void requireOrderLineItemNotEmpty(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private void requireMenusExist(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void requireOrderTableNotEmpty(Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        for (final Order order : orders) {
            for (OrderLineItem orderLineItem : orderLineItemDao.findAllByOrderId(order.getId())) {
                order.add(orderLineItem);
            }
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order order = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
        final Order updatedOrder = order.withOrderStatus(request.getOrderStatus());
        orderDao.save(updatedOrder);

        for (OrderLineItem orderLineItem : orderLineItemDao.findAllByOrderId(orderId)) {
            updatedOrder.add(orderLineItem);
        }
        return updatedOrder;
    }
}

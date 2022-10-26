package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        Order order = request.toOrder(OrderStatus.COOKING.name(), LocalDateTime.now());

        List<OrderLineItemRequest> itemRequests = request.getOrderLineItemRequests();
        if (CollectionUtils.isEmpty(itemRequests)) {
            throw new IllegalArgumentException();
        }
        final List<Long> menuIds = toMenuIds(itemRequests);
        if (itemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        order.changeTable(orderTable);

        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItem : itemRequests) {
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem.toOrderLineItem(orderId)));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private static List<Long> toMenuIds(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}

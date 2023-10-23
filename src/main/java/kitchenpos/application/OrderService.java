package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.CreateOrderResponse;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.mapper.OrderMapper;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public CreateOrderResponse create(final CreateOrderRequest request) {
        final OrderTable entity = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (entity.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItemIds());
        final Order order = OrderMapper.toOrder(request, orderLineItems);
        final Order savedOrder = orderDao.save(order);
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems, orderId);

        return CreateOrderResponse.of(
                savedOrder.updateOrderLineItems(savedOrderLineItems),
                savedOrderLineItems.stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems, Long orderId) {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.updateOrderId(orderId))
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getOrderLineItems(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        return menuIds.stream()
                .map(orderLineItemDao::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        final List<Order> result = new ArrayList<>();

        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            result.add(order.updateOrderLineItems(orderLineItems));
        }

        return result.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        Order updated = savedOrder.updateStatus(orderStatus);

        orderDao.save(updated);

        return OrderResponse.from(updated.updateOrderLineItems(orderLineItemDao.findAllByOrderId(orderId)));
    }
}

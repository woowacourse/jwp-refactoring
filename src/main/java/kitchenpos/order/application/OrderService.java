package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCompletedEvent;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderProceededEvent;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.request.OrderStatusRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao,
            final ApplicationEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = proceedOrder(request);
        validateEmptyTable(order.getOrderTableId());
        validateOrderMenus(order);
        eventPublisher.publishEvent(new OrderProceededEvent(order, order.getOrderTableId()));

        return OrderResponse.of(orderRepository.save(order));
    }

    private Order proceedOrder(final OrderRequest request) {
        final List<OrderLineItem> items = new ArrayList<>();

        for (final OrderLineItemRequest itemRequest : request.getOrderLineItems()) {
            final Menu menu = menuRepository.getById(itemRequest.getMenuId());
            items.add(new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), itemRequest.getQuantity()));
        }

        return Order.proceed(request.getOrderTableId(), items);
    }

    private void validateEmptyTable(final Long orderTableId) {
        if (getOrderTableById(orderTableId).isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderMenus(final Order order) {
        if (order.getItemSize() != menuRepository.countByIdIn(order.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.getById(orderId);
        final Order changeOrder = savedOrder.changeOrderStatus(request.getOrderStatus());

        orderRepository.save(changeOrder);
        eventPublisher.publishEvent(new OrderCompletedEvent(changeOrder, changeOrder.getOrderTableId()));

        return OrderResponse.of(changeOrder);
    }
}

package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = proceedOrder(request);
        validateEmptyTable(order.getOrderTableId());
        validateOrderMenus(order);

        return OrderResponse.of(orderRepository.save(order));
    }

    private static Order proceedOrder(final OrderRequest request) {
        final List<OrderLineItem> items = request.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());

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

        return OrderResponse.of(orderRepository.save(changeOrder));
    }
}
